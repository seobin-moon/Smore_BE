# JSON 객체를 생성하고 문자열로 변환
$jsonParams = @{
    host = @("smore-dev-redis-cluster.5w9cd3.0001.apn2.cache.amazonaws.com")
    portNumber = @("6379")
    localPortNumber = @("6379")
} | ConvertTo-Json -Compress

# 큰따옴표를 이스케이프하여 JSON을 AWS CLI에 올바르게 전달
$jsonParams = $jsonParams -replace '"', '\"'

# AWS SSM 명령어 실행
aws ssm start-session `
  --target i-02067e743bd9b406c `
  --document-name AWS-StartPortForwardingSessionToRemoteHost `
  --parameters "$jsonParams"
