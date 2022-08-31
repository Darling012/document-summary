node {
//    def releaseTag = "${projectVersion == '' ? BUILD_NUMBER : projectVersion}"
    def serviceName = "${projectName}"
    def groupName = "dbp-edu"
    // def registryHost = "harbor.deepblueai.com"
    def registryHost = "${dockerResp}"
    def gitRepo = 'http://zhangl@gerrit.deepblueai.com/a/JN_EDU'
    def gitCredentialsId = 'gerrit-credential'
    def fetchBranch = 'master'
    def serviceDirMap = [
            "all"                            : "",
            "all-cloud"                      : 'cloud',
            "all-edge"                       : 'edge',
            'service-exam'                   : 'cloud/service-exam-parent',
            'service-foundation'             : 'cloud/service-foundation-parent',
            'service-dict'                   : 'cloud/service-dict-parent',
            'service-account'                : 'cloud/service-account-parent',
            'gateway-management'             : 'cloud/gateway-management',
            'service-teach'                  : 'cloud/service-teach-parent',
            'service-job'                    : 'cloud/service-job',
            'service-oss'                    : 'cloud/service-oss',
            'bff-student-iot'                : 'cloud/bff-student-iot-parent',
            'bff-teacher-iot'                : 'cloud/bff-teacher-iot-parent',
            'bff-management'                : 'cloud/bff-management-parent',
            'bff-op'                         : 'cloud/bff-op',
            'edge-service-media'             : 'edge/edge-service-media-parent',
            'edge-server-algorithm-gateway'  : 'edge/edge-server-algorithm-gateway',
            'edge-service-algorithm-dispatch': 'edge/edge-service-algorithm-dispatch-parent',
            'edge-service-file-manager'      : 'edge/edge-service-file-manager-parent',
            'edge-service-student-iot'       : 'edge/edge-service-student-iot',
            'edge-service-teacher-iot'       : 'edge/edge-service-teacher-iot',
            'edge-service-video-process'     : 'edge/edge-service-video-process-parent',
            'edge-service-biz'               : 'edge/edge-service-biz-parent',
            'edge-service-k12'               : 'edge/edge-service-k12-parent'

    ]
    // stage('Clean workspace') {
    //     cleanWs()
    // }

    stage('Git clone codes') {
        git credentialsId: gitCredentialsId, branch: fetchBranch, url: gitRepo, poll: false
    }

    stage("Maven build service") {

        String serviceDir = serviceDirMap[serviceName]
        println('whether is a gather service::::' + !serviceDir.endsWith('-parent'))
        dir("./backend/${serviceDir}") {
//            withMaven(maven: 'maven_3.5.2') {
            sh """
                        mvn clean install -Dmaven.test.skip=true --settings /opt/settings.xml
                    """
//            }
        }
    }

    stage('Build & push images') {
        println "start build and push images of service:[${serviceName}]"
        if (serviceName == "all-cloud" || serviceName == "all") {
            println "build images of all cloud services..."
            serviceDirMap.each {
                svcName, svcDir ->
                    // 跳过所有all和edge开头的
                    if (svcName.startsWith("all") || svcName.startsWith("edge-")) {
                        return true
                    }
                    String mainDir = "./backend/${svcDir}"
                    if (svcDir.endsWith('-parent')) {
                        mainDir = mainDir.plus("/${svcName}")
                    }
                    println "start build image of service:[${svcName}], mainDir:[${mainDir}]"
                    dir("${mainDir}") {
                        def serviceImage = docker.build("${registryHost}/${groupName}/${svcName}:${releaseTag}")
                        println "start push image of service:[${svcName}], mainDir:[${mainDir}]"
                        docker.withRegistry("https://${registryHost}", 'core-jcr-credential') {
                            serviceImage.push()
//                            serviceImage.push('latest')
                        }
                    }
            }
        }
        if (serviceName == "all-edge" || serviceName == "all") {
            println "build images of all edge services..."
            serviceDirMap.each {
                svcName, svcDir ->
                    //  先跳过用不到服务
                    if (svcName.startsWith("edge-server-algorithm-gateway")
                            || svcName.startsWith("edge-service-algorithm-dispatch")
                            || svcName.startsWith("edge-service-file-manager")
                            || svcName.startsWith("edge-service-video-process")
                            || svcName.startsWith("edge-service-k12")
                    ) {
                        println "跳过的maven模块:[${svcName}]"
                        return true
                    }
                    if (!svcName.startsWith("edge-")) {
                        return true
                    }
                    String mainDir = "./backend/${svcDir}"
                    if (svcDir.endsWith('-parent')) {
                        mainDir = mainDir.plus("/${svcName}")
                    }
                    println "start build image of service:[${svcName}], mainDir:[${mainDir}]"
                    dir("${mainDir}") {
                        def serviceImage = docker.build("${registryHost}/${groupName}/${svcName}:${releaseTag}")
                        docker.withRegistry("https://${registryHost}", 'core-jcr-credential') {
                            serviceImage.push()
                        }
                    }
            }
        }

        if (!serviceName.startsWith("all")) {
            println "build image of service[${serviceName}]"
            String serviceDir = serviceDirMap[serviceName]
            String mainDir = "./backend/${serviceDir}"
            println "mainDir值为:" + mainDir
            if (serviceDir.endsWith('-parent')) {
                mainDir = mainDir.plus("/${serviceName}")
            }
            println "mainDir值为:" + mainDir
            dir("${mainDir}") {
                println "当前所在目录为:" + mainDir
                def serviceImage = docker.build("${registryHost}/${groupName}/${serviceName}:${releaseTag}")
                docker.withRegistry("https://${registryHost}", 'core-jcr-credential') {
                    serviceImage.push()
//                    serviceImage.push('latest')
                }
            }
        }

    }

//    stage('Deploy service') {
//        println "It's success to deploy service named ${serviceName}"
//    }
//
//    stage('Clean images') {
//        sh "docker system prune -f"
//    }
}
