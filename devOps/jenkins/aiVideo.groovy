#!groovy
pipeline {
    agent any
    environment {
        PATH = "/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin"
    }
    options {
        timestamps()    //设置在项目打印日志时带上对应时间
        disableConcurrentBuilds()   //不允许同时执行流水线，被用来防止同时访问共享资源等
        timeout(time: 5, unit: 'MINUTES')   // 设置流水线运行超过n分钟，Jenkins将中止流水线
        buildDiscarder(logRotator(numToKeepStr: '20'))   // 表示保留n次构建历史
    }
    parameters {
        choice(
                choices: 'dev\ncompose',
                description: '请选择构建环境',
                name: 'profile'
        )
        choice(
                choices: 'all\ndeep-blue-aivideo-biz\ndeep-blue-gateway\ndeep-blue-auth\ndeep-blue-upms',
                description: '请选择构建模块',
                name: 'module'
        )
    }
    stages {
        stage('git clone code') {
            steps {
                git credentialsId: 'da3d6769-3701-420a-a983-58b393c1828e',
                        branch: 'master',
                        url: 'http://zhangl@gerrit.deepblueai.com/a/JN_AiVideo_AnGangPackagingFactory',
                        poll: false
            }
        }
//        stage('dir workspace') {
//            steps {
//                echo '切换当前工作目录到源码文件夹'
//                dir('./source/deep-blue-aivideo') {
//                    echo pwd
//                    echo 'maven build service'
//                    script {
//                        if (module == 'all') {
//                            echo "构建整个项目"
//                            sh 'mvn  -Dmaven.test.skip=true --settings /opt/settings.xml -P $profile clean package'
//                        } else {
//                            echo "构建单个模块$module"
//                            sh 'mvn -Dmaven.test.skip=true   clean package  -P $profile  -pl $module/$module-biz -am '
//                            echo "切换当前工作目录到dockerfile目录"
//                            dir('./' + module + '/' + module + '-biz') {
//                                echo pwd
//                                // docker.build("$module:1.0.0")
//                            }
//                        }
//                    }
//                }
//            }
//        }
        stage('ssh remote') {
            steps {
                script {
                    stage('远程登录更新镜像') {
                        withCredentials([usernamePassword(credentialsId: "66root", usernameVariable: "username", passwordVariable: "password")]) {
                            def remote = [:]
                            remote.name = '66服务器'
                            remote.host = '192.168.215.66'
                            remote.user = username
                            remote.password = password
                            remote.allowAnyHosts = true
//                        sshScript remote: remote,script: "pwd"
//                            不成功
                            sshCommand remote: remote, command: "cd /"
                            sshCommand remote: remote, command: "pwd"
                        }

                    }
                }
            }
        }
    }
}
