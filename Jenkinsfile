node {

    stage('Version info') {
        sh 'java -version'
        sh 'gradle -version'
    }

    stage('Build and unit test') {
        sh 'gradle clean build'
    }

    stage('Create jfx jar') {
        sh 'gradle jfxJar'
    }

}