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
                bat 'start "" /B java -jar target\\car-0.0.1-SNAPSHOT.jar --server.port=8081 > app.log 2>&1'
            }
        }

        stage('Wait for App') {
            steps {
                bat '''
                :loop
                curl -s http://localhost:8081/login >nul
                if errorlevel 1 (
                    timeout /t 2 >nul
                    goto loop
                )
                '''
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

        stage('Docker Build') {
            steps {
                bat 'docker build -t car-rental-app .'
            }
        }

        stage('Docker Run') {
            steps {
                bat '''
                for /f %%i in ('docker ps -q') do docker stop %%i >nul 2>&1
                docker run -d -p 8082:8081 car-rental-app
                '''
            }
        }
    }
}