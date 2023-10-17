# keycloak

#### user:

系统中登录用户，通过user添加自定义属性来关联业务系统中的用户。在user的role mappings里配置与roles的关联。

##### roles:

role分为两种，一种归属relam，一种归属client，我们一般针对client设置。

##### policies:

policies将角色与权限关联起来，在policy里关联角色，与权限的关联在permissions里。

##### permissions:

访问资源所需权限，关联policy，resources,scope

##### scopes:

用于区分相同URL的get、post等请求方式不同，权限不同

##### resources:

即资源，与scopes产生关联