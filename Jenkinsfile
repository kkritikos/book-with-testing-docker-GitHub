node {
    checkout scm
    /*
     * In order to communicate with the MySQL server, this Pipeline explicitly
     * maps the port (`3306`) to a known port on the host machine.
     */
    docker.image('mysql:5').withRun('-e "MYSQL_ROOT_PASSWORD=my-secret-pw" -p 3306:3306') { c ->
        /* Wait until mysql service is up */
        sh 'while ! mysqladmin ping -h0.0.0.0 --silent; do sleep 1; done'
        /* Run some tests which require MySQL */
        sh 'make check'
    }
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
