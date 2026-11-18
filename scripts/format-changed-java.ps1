$payload = [Console]::In.ReadToEnd()
if ($payload -match 'src[\\/]((main)|(test))[\\/]java[\\/]') {
  & .\mvnw.cmd -q spotless:apply
  exit $LASTEXITCODE
}
exit 0
