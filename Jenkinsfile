pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    stages {

        stage('Build') {
            steps {
                bat 'mvn clean install -DskipTests'
            }
        }

        stage('Start Application') {
            steps {
                // Start app and save logs
                bat 'start "" /B java -jar target\\car-0.0.1-SNAPSHOT.jar --server.port=8081 > app.log 2>&1'

                // Wait longer
                bat 'ping 127.0.0.1 -n 60'

                // PRINT LOG (VERY IMPORTANT)
                bat 'type app.log'
            }
        }

        stage('Run Selenium Tests') {
            steps {
                bat 'mvn test -Dtest=SeleniumTest'
            }
        }

        stage('Stop Application') {
            steps {
                bat '''
                for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8081') do (
                    if not "%%a"=="0" (
                        taskkill /F /PID %%a >nul 2>&1
                    )
                )
                exit 0
                '''
            }
        }
    }
}