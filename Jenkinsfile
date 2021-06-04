node {
    checkout scm
    /*
     * In order to communicate with the MySQL server, this Pipeline explicitly
     * maps the port (`3306`) to a known port on the host machine.
     */
    stage('Init'){
        steps {
           echo "Build is starting!!!"
        }
    }
    
    def mvnImage = docker.image('maven:3.8.1-adoptopenjdk-11')
    stage('Build') {
        steps {
            mvnImage.inside(){
                sh 'mvn -B -DskipTests clean package'
            }

        }
    }

    def sqlImage = docker.build("mysql:latest", "-f Dockerfile_mysql .")
    sqlImage.run('-d --name mysql -p 3306:3306 mysql:latest')
    def tomcatImage = docker.build("mytomcat:latest", "-f Dockerfile .")
    tomcatImage.run('-d --name mytomcat -p 8090:8090 mytomcat:latest')
}

/*
pipeline {
    agent {
        docker {
            image 'maven:3.8.1-adoptopenjdk-11'
            args '-v /root/.m2:/root/.m2 -v /var/run/docker.sock:/var/run/docker.sock --privileged'
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
            steps {	
                sh 'export DOCKER_HOST=unix:///var/run/docker.sock; mvn verify' 
                //sh 'export DOCKER_HOST=tcp://127.0.0.1:2375; mvn verify' 
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
}*/
