node {
    checkout scm
    
    def mvnImage = docker.image('maven:3.8.1-adoptopenjdk-11')
    def postfix
    def label = 'kkritikos/book:'
    if (env.BRANCH_NAME=='master'){
         label += 'latest-stable'
         postfix='prod'
    }
    else{
         label += ('dev' + env.BUILD_ID)
         postfix='dev' + env.BUILD_ID
    }
    	 
    stage('Init_Clean'){
    	   mvnImage.inside(){
		     sh 'echo "Build is starting!!!"'    	       
    	   }
    	   try{
    	    	sh 'docker container stop mysql_' + postfix + ' tomcat_' + postfix   
    	   }
    	   catch(e){}
    	   sh 'docker system prune -f'
    	   
    }
    
    def sqlImage
    def tomcatImage
    stage('Build') {
            mvnImage.inside(){
                sh 'mvn -B -DskipTests clean package'
            }
            sqlImage = docker.build("mysql:latest", "-f Dockerfile_mysql .")
            tomcatImage = docker.build(label, "-f Dockerfile .")
    }
    
    stage('Test'){
        try{
        	sh 'docker network create book-net_' + postfix
        	sh 'docker run -d --name mysql_' + postfix + ' --network book-net_' + postfix + ' --network-alias mysql mysql:latest'
        	sh 'docker run -d --name tomcat_' + postfix + ' --network book-net_' + postfix + ' --network-alias tomcat -p 8090:8090 ' + label
        	mvnImage.inside('--network book-net_' + postfix){
      			sh 'mvn verify'   
        	}    
        }
        catch(e){
        	echo 'Something went wrong during testing'
        	throw e    
        }
		finally{
		    try{
				sh 'docker container stop mysql_' + postfix + ' tomcat_' + postfix
				sh 'docker container rm mysql_' + postfix + ' tomcat_' + postfix
				sh 'docker system prune -f'		        
		    }
		    catch(e){
		        echo 'Something went wrong while destroying the testing containers'
		    }

		    mvnImage.inside(){
                	junit 'book-functional-tests/target/failsafe-reports/*.xml'
            }
		}
    }
    
    stage('Push') {
    	docker.withRegistry('https://index.docker.io/v1/', 'github-cred') {
        	tomcatImage.push()
        }
    }
}