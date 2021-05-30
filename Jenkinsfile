pipeline {
    agent {
        docker {
            image 'maven:3.8.1-adoptopenjdk-11'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
    	stage('Init') {
            steps {
                echo "Build is starting!!!"
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
        	environment { 
        		DOCKER_HOST = 'tcp://127.0.0.1:2375'
    		}
        	
            steps {	
                sh 'mvn verify' 
            }
            post {
                always {
                    junit 'book-functional-tests/target/failsafe-reports/*.xml' 
                }
            }
        }
        stage('Install_Development') {
        	when {
	            environment name: 'DEPLOY_TO', value: 'development'
	        }
            steps {
                sh "mvn -B -DskipTests -DskipITs install -DskipImage -Dcargo.hostname=${hostname} -Dcargo.protocol=${protocol} -Dcargo.servlet.port=${port} -Dcargo.remote.username=${user} -Dcargo.remote.password=${pass}"
                sh('''
                    git config --local credential.helper "!f() { echo username=\\$GIT_AUTH_USR; echo password=\\$GIT_AUTH_PSW; }; f"
                    git push origin HEAD:$TARGET_BRANCH
                ''')
            }
        }
        stage('Install_Production') {
        	when {
	            environment name: 'DEPLOY_TO', value: 'production'
	        }
            steps {
                sh "mvn -B -DskipTests -DskipITs -Dregistry.username=${user} -Dregistry.password=${password} install"
                sh('''
                    git config --local credential.helper "!f() { echo username=\\$GIT_AUTH_USR; echo password=\\$GIT_AUTH_PSW; }; f"
                    git push origin HEAD:$TARGET_BRANCH
                ''')
            }
        }
    }
}
