#!groovy
pipeline {
    agent any
    environment {
        registryUrl = "192.168.1.110:5000"       //搭建docker私有仓库（Harbor）或者 用DockerHub 又或者用云平台的“容器镜像服务”
        registry_user = "xxx"
        registry_pass = "xxx"
    }
    options {
        timestamps()    //设置在项目打印日志时带上对应时间
        disableConcurrentBuilds()   //不允许同时执行流水线，被用来防止同时访问共享资源等
        timeout(time: 5, unit: 'MINUTES')   // 设置流水线运行超过n分钟，Jenkins将中止流水线
        buildDiscarder(logRotator(numToKeepStr: '20'))   // 表示保留n次构建历史
    }

    //gitlab  webhook触发器
    //聚合项目，代码发生以下动作后，所有子项目将被触发构建,可选择使用（前提需要gitlab配置 webhook）
    //triggers{    //方法一，为All时
    //   gitlab( triggerOnPush: true,                       //代码有push动作就会触发job
    //       triggerOnMergeRequest: true,                   //代码有merge动作就会触发job
    //        branchFilterType: "All")                      //为All时（只有符合条件的分支才会触发构建 “All/NameBasedFilter/RegexBasedFilter”）
    //}
    //triggers{    //方法二，为branchFilterType时
    //   gitlab( triggerOnPush: true,
    //       triggerOnMergeRequest: true,
    //        branchFilterType: "branchFilterType",          //为branchFilterType时
    //        includeBranchesSpec: "dev")                  //基于branchFilterType值，输入期望包括的分支的规则
    //}

    stages {
        stage('Print Message') {      //打印信息
            steps {
                echo '打印信息'
                echo "Project_Pipeline_name: ${JOB_NAME}"
                echo "Project_module_name: ${PROJECT_NAME}"
                echo "workspace: ${WORKSPACE}"
                echo "branch: ${Branch_name}"           //gitlab分支名
                echo "build_id: ${BUILD_ID}"
                echo "target_action: ${action}"
                echo "registryUrl: ${registryUrl}"
                echo "image_repository: ${registryUrl}/${Project_name}"
            }
        }
        //此步骤在调试Jenkinsfile时可以注释以便了解目录结构
        stage('Delete Workspace') {         //清理工作目录
            steps {
                echo "清理工作目录: ${WORKSPACE}"
                deleteDir()     //表示删除当前目录(${WORKSPACE})下内容，通常用在构建完毕之后清空工作空间
            }
        }
        stage('Checkout') {         //拉取代码
            steps {
                echo '拉取代码'
                script {
                    if (action == 'deploy') {    //判断当action == 'deploy' 时，才执行此stage
                        checkout([$class : 'GitSCM', branches: [[name: '${Branch_name}']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [],
                                  userRemoteConfigs: [[credentialsId: '7ff3778d-124f-40b1-a5e6-05d600a7e59e', //gitlab登录令牌，如何设置自行搜索方法
                                                       url          : 'http://192.168.1.110/java/pipeline-project.git']]])
                        //gitlab项目clone地址
                    }
                }
            }
        }
        stage('Packaging project') {        //mvn打包
            steps {
                echo 'mvn打包子项目'
                script {
                    if (action == 'deploy') {
                        sh 'source /etc/profile && mvn clean package -pl ${Project_name} -am -amd -P${Branch_name} -Dmaven.test.skip=true'
                        //“-pl”指定子项目名称 ； “-P”指定使用哪个环节的配置文件（-Ptest即表示使用文件application-test.yml配置文件打包）
                    }
                }
            }
        }
        stage('Build & Push Image to Harbor') {      //构建，推送镜像
            steps {
                echo '构建，推送镜像到docker镜像仓库'
                dir('./') {       //指定工作目录（默认为${WORKSPACE}）
                    script {
                        if (action == 'deploy') {
                            //方法一：
                            //sh 'docker login  --username=${registry_user} --password=${registry_pass}   ${registryUrl}'
                            //sh 'cp ${Project_name}/target/*.jar ./'
                            //sh 'docker build  -t ${registryUrl}/${Project_name}:${Branch_name}-${BUILD_ID} -f ./Dockerfile_config/${Project_name}/Dockerfile . '
                            //sh 'docker push ${registryUrl}/${Project_name}:${Branch_name}-${BUILD_ID}'
                            //方法二：
                            sh 'docker login  --username=${registry_user} --password=${registry_pass}   ${registryUrl}'
                            sh 'cp ${Project_name}/target/*.jar ./ '
                            def app = docker.build('${registryUrl}/${Project_name}:${Branch_name}-${BUILD_ID} -f ./Dockerfile_config/${Project_name}/Dockerfile')
                            app.push('${Branch_name}-${BUILD_ID}')

                            //sh 'docker rmi ${registryUrl}/${Project_name}:${Branch_name}-${BUILD_ID}'
                        }
                    }
                }
            }
        }
        stage('Deploy to the Target server') {      //部署到目标服务器（$action == deploy/restart/rollback）
            steps {
                echo '部署到目标服务器'
                script {
                    timeout(time: 40, unit: 'SECONDS') {    // 设置远程部署超过n秒，将终止该步骤
                        sh 'bash  ./Deploy.sh  ${Project_name}  ${registryUrl}/${Project_name}:${Branch_name}  ${Branch_name}  ${action}  ${BUILD_ID} ${rollback_id}'
                        //${1,2,3,4,5,6}
                    }
                }
            }

        }
    }
}
