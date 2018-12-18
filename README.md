# ITSM
ITSM System


# 心路历程
## 2018-7-23
1. 目前无法知道iframe的开始加载的事件，所以只能先用在a标签里面用遮罩，然后再iframe中的onload事件来关闭。
2. 修复了用户头像的问题。目前用户修改头像后，会同步更新。


## 2017-12-22
1. 增加ticket虚拟类，相当于将request，incident，problem，change四种ticket合并在一起作为一个列表


## 2017-12-29
1. 因为考虑到 contact 所属的 group 是有可能多个，所以不直接将 group 的信息直接放在 contact 