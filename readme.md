**注意**
* LoadingActivity  命名不要变更  action不要删除
* SDKCallback 包结构不要变更（即目录）

其中commolibs不涉及相关业务和UI层的信息
commonuis涉及到相关activity和fragment的基类信息，实现了mvvm+mvp
photo为实现了拍照+本地选择照片的逻辑
record为实现了录音，视频的功能
update为升级相关业务

各module引用关系为：
lib被所有其他的module引用
其他各module不相互引用