- open app (launcher activity)

```shell
adb shell am start -n home.smart.fly.animations/home.smart.fly.animations.AppStartActivity
```

- open app (any activity)

```xml

<activity android:name=".ui.activity.BitmapMeshActivity" android:exported="true" />
```

要打开的 Activity 必须配置 exported = "true"

```shell
adb shell am start -n home.smart.fly.animations/home.smart.fly.animations.ui.activity.BitmapMeshActivity
```

否则强行打开，会抛出异常

```shell
adb shell am start -n home.smart.fly.animations/home.smart.fly.animations.ui.activity.OrientationActivity
```

```shell
Exception occurred while executing 'start':
java.lang.SecurityException: Permission Denial: starting Intent { flg=0x10000000 cmp=home.smart.fly.animations/.ui.activity.OrientationActivity } from null (pid=5266, uid=2000) not exported from uid 10770
        at com.android.server.wm.ActivityStackSupervisor.checkStartAnyActivityPermission(ActivityStackSupervisor.java:1157)
        at com.android.server.wm.ActivityStarter.executeRequest(ActivityStarter.java:1244)
        at com.android.server.wm.ActivityStarter.execute(ActivityStarter.java:754)
        at com.android.server.wm.ActivityTaskManagerService.startActivityAsUser(ActivityTaskManagerService.java:1169)
        at com.android.server.wm.ActivityTaskManagerService.startActivityAsUser(ActivityTaskManagerService.java:1141)
        at com.android.server.am.ActivityManagerService.startActivityAsUserWithFeature(ActivityManagerService.java:4170)
        at com.android.server.am.ActivityManagerShellCommand.runStartActivity(ActivityManagerShellCommand.java:549)
        at com.android.server.am.ActivityManagerShellCommand.onCommand(ActivityManagerShellCommand.java:191)
        at android.os.BasicShellCommandHandler.exec(BasicShellCommandHandler.java:98)
        at android.os.ShellCommand.exec(ShellCommand.java:44)
        at com.android.server.am.ActivityManagerService.onShellCommand(ActivityManagerService.java:12094)
        at android.os.Binder.shellCommand(Binder.java:940)
        at android.os.Binder.onTransact(Binder.java:824)
        at android.app.IActivityManager$Stub.onTransact(IActivityManager.java:5180)
        at com.android.server.am.ActivityManagerService.onTransact(ActivityManagerService.java:3283)
        at android.os.Binder.execTransactInternal(Binder.java:1170)
        at android.os.Binder.execTransact(Binder.java:1134)
```