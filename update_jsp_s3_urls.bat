@echo off
echo 전체 JSP 파일에서 S3 URL을 환경변수로 변경합니다...

REM PowerShell을 사용하여 모든 JSP 파일의 S3 URL을 환경변수로 변경
powershell -Command "Get-ChildItem -Path 'c:\Users\kosta\git\GreenTable\src\main\webapp' -Recurse -Filter '*.jsp' | ForEach-Object { (Get-Content $_.FullName) -replace 'https://greentable-images\.s3\.ap-northeast-2\.amazonaws\.com', '${s3BaseUrl}' | Set-Content $_.FullName }"

echo JSP 파일 S3 URL 변경 완료!
