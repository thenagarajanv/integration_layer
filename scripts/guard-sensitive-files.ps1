$payload = [Console]::In.ReadToEnd()
$blockedPatterns = @('(^|[\\/])\.env$', '(^|[\\/])application-.*\.yml$', '(^|[\\/])secrets?([\\/]|$)', '(^|[\\/])\.ssh([\\/]|$)', '\.(pem|key|p12|pfx)$')
foreach ($pattern in $blockedPatterns) {
  if ($payload -match $pattern) {
    [Console]::Error.WriteLine('NexusFlow policy blocks AI writes to sensitive configuration or key material.')
    exit 2
  }
}
exit 0
