# This script performs the Register and Login sequence and outputs the resulting JWT token.
# It MUST be run from the project root directory after the Spring Boot app is started.

# --- CONFIGURATION ---
$BASE_URL = "http://localhost:8080/api/auth"
$TEST_PASS = "SecurePassword123!"

# Generate a unique email timestamp to prevent "Email already in use" errors
$timestamp = (Get-Date -Format "yyyyMMddHHmmss")
$TEST_EMAIL = "login_test_$timestamp@smarttask.com"

# Standard headers for JSON communication
$HEADERS = @{"Content-Type" = "application/json"}

Function Test-LoginFlow {
    Write-Host "================================================" -ForegroundColor Cyan
    Write-Host "   REGISTER & LOGIN TOKEN TEST" -ForegroundColor Cyan
    Write-Host "================================================" -ForegroundColor Cyan
    Write-Host "Test Email: $TEST_EMAIL`n" -ForegroundColor Yellow
    
    # --- PHASE 1: REGISTER ---
    Write-Host "--- 1. Testing Registration (POST /register) ---" -ForegroundColor Yellow
    $registerBody = @{
        firstName = "Login";
        lastName = "Tester";
        email = $TEST_EMAIL;
        password = $TEST_PASS;
    } | ConvertTo-Json

    try {
        # Execute the registration command
        Invoke-RestMethod -Method Post -Uri "$BASE_URL/register" -Headers $HEADERS -Body $registerBody
        Write-Host "STATUS: 201 CREATED (Success)" -ForegroundColor Green
    } catch {
        Write-Host "Registration Failed: $($_.Exception.Message)" -ForegroundColor Red
        return
    }

    # --- PHASE 2: LOGIN ---
    Write-Host "`n--- 2. Testing Login (POST /login) ---" -ForegroundColor Yellow
    $loginBody = @{
        email = $TEST_EMAIL;
        password = $TEST_PASS;
    } | ConvertTo-Json

    try {
        # Execute the login command and capture the result
        $loginResult = Invoke-RestMethod -Method Post -Uri "$BASE_URL/login" -Headers $HEADERS -Body $loginBody
        
        # Extract and display the token
        $token = $loginResult.token
        Write-Host "STATUS: 200 OK (Token Received)" -ForegroundColor Green
        Write-Host "`n================ JWT TOKEN ===============" -ForegroundColor Yellow
        Write-Host $token -ForegroundColor White
        Write-Host "==========================================" -ForegroundColor Yellow
        
    } catch {
        Write-Host "Login Failed: $($_.Exception.Message)" -ForegroundColor Red
        return
    }

    Write-Host "`nAuthentication flow complete." -ForegroundColor Cyan
}

Test-LoginFlow