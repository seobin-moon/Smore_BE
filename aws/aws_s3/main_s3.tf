terraform {
  // aws 라이브러리 불러옴
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

# AWS 설정 시작
provider "aws" {
  region = var.region
}
# AWS 설정 끝

# VPC 설정 시작
resource "aws_vpc" "vpc_1" {
  cidr_block = "10.0.0.0/16"

  # 무조건 켜세요.
  enable_dns_support   = true
  # 무조건 켜세요.
  enable_dns_hostnames = true

  tags = {
    Name = "${var.prefix}-vpc-1"
  }
}

resource "aws_subnet" "subnet_1" {
  vpc_id                  = aws_vpc.vpc_1.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "${var.region}a"
  map_public_ip_on_launch = true

  tags = {
    Name = "${var.prefix}-subnet-1"
  }
}

resource "aws_subnet" "subnet_2" {
  vpc_id                  = aws_vpc.vpc_1.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "${var.region}b"
  map_public_ip_on_launch = true

  tags = {
    Name = "${var.prefix}-subnet-2"
  }
}

resource "aws_subnet" "subnet_3" {
  vpc_id                  = aws_vpc.vpc_1.id
  cidr_block              = "10.0.3.0/24"
  availability_zone       = "${var.region}c"
  map_public_ip_on_launch = true

  tags = {
    Name = "${var.prefix}-subnet-3"
  }
}

resource "aws_internet_gateway" "igw_1" {
  vpc_id = aws_vpc.vpc_1.id

  tags = {
    Name = "${var.prefix}-igw-1"
  }
}

resource "aws_route_table" "rt_1" {
  vpc_id = aws_vpc.vpc_1.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw_1.id
  }

  tags = {
    Name = "${var.prefix}-rt-1"
  }
}

resource "aws_route_table_association" "association_1" {
  subnet_id      = aws_subnet.subnet_1.id
  route_table_id = aws_route_table.rt_1.id
}

resource "aws_route_table_association" "association_2" {
  subnet_id      = aws_subnet.subnet_2.id
  route_table_id = aws_route_table.rt_1.id
}

resource "aws_route_table_association" "association_3" {
  subnet_id      = aws_subnet.subnet_3.id
  route_table_id = aws_route_table.rt_1.id
}

resource "aws_security_group" "sg_1" {
  name = "${var.prefix}-sg-1"

  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "all"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "all"
    cidr_blocks = ["0.0.0.0/0"]
  }

  vpc_id = aws_vpc.vpc_1.id

  tags = {
    Name = "${var.prefix}-sg-1"
  }
}

# EC2 설정 시작

# EC2 역할 생성
resource "aws_iam_role" "ec2_role_1" {
  name = "${var.prefix}-ec2-role-1"

  # 이 역할에 대한 신뢰 정책 설정. EC2 서비스가 이 역할을 가정할 수 있도록 설정
  assume_role_policy = <<EOF
  {
    "Version": "2012-10-17",
    "Statement": [
      {
        "Sid": "",
        "Action": "sts:AssumeRole",
        "Principal": {
            "Service": "ec2.amazonaws.com"
        },
        "Effect": "Allow"
      }
    ]
  }
  EOF
}

# EC2 역할에 AmazonS3FullAccess 정책을 부착
resource "aws_iam_role_policy_attachment" "s3_full_access" {
  role       = aws_iam_role.ec2_role_1.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3FullAccess"
}

# EC2 역할에 AmazonEC2RoleforSSM 정책을 부착
resource "aws_iam_role_policy_attachment" "ec2_ssm" {
  role       = aws_iam_role.ec2_role_1.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2RoleforSSM"
}

# IAM 인스턴스 프로파일 생성
resource "aws_iam_instance_profile" "instance_profile_1" {
  name = "${var.prefix}-instance-profile-1"
  role = aws_iam_role.ec2_role_1.name
}

locals {
  ec2_user_data_base = <<-END_OF_FILE
#!/bin/bash
yum install docker -y
systemctl enable docker
systemctl start docker

curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

yum install git -y

sudo dd if=/dev/zero of=/swapfile bs=128M count=32
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
sudo swapon -s
sudo sh -c 'echo "/swapfile swap swap defaults 0 0" >> /etc/fstab'

END_OF_FILE
}

# EC2 인스턴스 생성
resource "aws_instance" "ec2_1" {
  # 사용할 AMI ID
  ami                         = "ami-07eff2bc4837a9e01"
  # EC2 인스턴스 유형
  instance_type               = "t2.micro"
  # 사용할 서브넷 ID
  subnet_id                   = aws_subnet.subnet_1.id
  # 적용할 보안 그룹 ID
  vpc_security_group_ids      = [aws_security_group.sg_1.id]
  # 퍼블릭 IP 연결 설정
  associate_public_ip_address = true

  # 인스턴스에 IAM 역할 연결
  iam_instance_profile = aws_iam_instance_profile.instance_profile_1.name

  # 인스턴스에 태그 설정
  tags = {
    Name = "${var.prefix}-ec2-1"
  }

  # 루트 볼륨 설정
  root_block_device {
    volume_type = "gp3"
    volume_size = 32  # 볼륨 크기를 32GB로 설정
  }

  # User data script for ec2_1
  user_data = <<-EOF
${local.ec2_user_data_base}
EOF
}

# S3 설정 시작
resource "aws_s3_bucket" "bucket" {
  bucket = "${var.prefix}-bucket-${var.nickname}-1"

  tags = {
    Name = "${var.prefix}-bucket-${var.nickname}-1"
  }
}

data "aws_iam_policy_document" "bucket_1_policy_1_statement" {
  statement {
    sid    = "PublicReadGetObject"
    effect = "Allow"

    principals {
      type        = "AWS"
      identifiers = ["*"]
    }

    actions   = ["s3:GetObject"]
    resources = ["${aws_s3_bucket.bucket.arn}/*"]
  }
}

resource "aws_s3_bucket_policy" "bucket_1_policy_1" {
  bucket = aws_s3_bucket.bucket.id

  policy = data.aws_iam_policy_document.bucket_1_policy_1_statement.json

  depends_on = [aws_s3_bucket_public_access_block.bucket_1_public_access_block_1]
}

resource "aws_s3_bucket_public_access_block" "bucket_1_public_access_block_1" {
  bucket = aws_s3_bucket.bucket.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

# 게시물 첨부 파일 업로드 예시
resource "aws_s3_object" "study_post_file" {
  bucket       = aws_s3_bucket.bucket.id
  key          = "articles/study1_attachment.pdf"  # 게시물 첨부 파일 articles 폴더 업로드
  content      = "Study article attachment content"  # 실제 파일이 아닌 예시 텍스트
  content_type = "application/pdf"
}

# 유저 프로필 이미지 업로드 예시
resource "aws_s3_object" "user_profile_image" {
  bucket       = aws_s3_bucket.bucket.id
  key          = "user_profiles/user1.jpg"  # 유저 프로필 이미지 user_profiles 폴더 업로드
  content      = "User profile image content"  # 실제 이미지 대신 예시 텍스트
  content_type = "image/jpeg"
}

# 스터디 대표 프로필 이미지 업로드 예시
resource "aws_s3_object" "study_profile_image" {
  bucket       = aws_s3_bucket.bucket.id
  key          = "study_profiles/study1_logo.jpg"  # 스터디 대표 이미지 study_profiles 폴더 업로드
  content      = "Study logo image content"  # 실제 이미지 대신 예시 텍스트
  content_type = "image/jpeg"
}
# S3 설정 끝