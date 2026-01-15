## 安装 bsdiff

下面按**不同操作系统**给你一份**最常用、最稳妥的 bsdiff 安装方式**。
（`bsdiff` 本身是一个 C 工具，没有复杂依赖）

---

## 一、macOS 安装（最简单 ✅）

### 方法 1：Homebrew（**强烈推荐**）

```bash
brew install bsdiff
```

验证：

```bash
bsdiff --help
bspatch --help
```

安装位置一般是：

```bash
/usr/local/bin/bsdiff
```

---

### 方法 2：源码编译（可控、适合定制）

```bash
git clone https://github.com/mendsley/bsdiff.git
cd bsdiff
make
sudo make install
```

---

## 二、Ubuntu / Debian / Linux 安装

### 方法 1：apt（部分发行版有）

```bash
sudo apt update
sudo apt install bsdiff
```

如果找不到包（很常见），用源码方式 👇

---

### 方法 2：源码编译（通用）

```bash
sudo apt install build-essential
git clone https://github.com/mendsley/bsdiff.git
cd bsdiff
make
sudo cp bsdiff bspatch /usr/local/bin/
```

验证：

```bash
which bsdiff
```

---

## 三、Windows 安装（3 种方式）

### ✅ 方案 1：WSL（最推荐）

```powershell
wsl --install
```

进入 WSL 后：

```bash
sudo apt install bsdiff
```

👉 **最接近 Linux / Android 构建环境**

---

### 方案 2：MinGW 编译

```bash
git clone https://github.com/mendsley/bsdiff.git
cd bsdiff
gcc bsdiff.c -o bsdiff
gcc bspatch.c -o bspatch
```

> 注意：需要 `bzip2` 或 `libbz2`

---

### 方案 3：直接下载预编译版（不推荐生产）

* GitHub release
* 第三方工具包

⚠️ **不利于 Android / CI / 自动化**

---

## 四、Android / NDK 环境（非常常见）

### 1️⃣ 编译为 Android 可执行文件

```bash
$NDK/toolchains/llvm/prebuilt/darwin-x86_64/bin/clang \
    bspatch.c \
    -o bspatch \
    --target=aarch64-linux-android21
```

推送到设备：

```bash
adb push bspatch /data/local/tmp/
adb shell chmod +x /data/local/tmp/bspatch
```

---

### 2️⃣ 编译为 JNI（推荐方式）

```text
libbspatch.so
```

Java / Kotlin 调用：

```java
native int bspatch(String oldFile, String newFile, String patch);
```

（如果你需要，我可以直接给你 **完整 NDK + CMake 示例**）

---

## 五、Docker（构建 / CI 专用）

```dockerfile
FROM ubuntu:22.04
RUN apt update && apt install -y bsdiff
```

适合：

* GitHub Actions
* 自动生成 patch

---

## 六、验证安装是否成功（必做）

```bash
echo "hello" > a.txt
echo "hello world" > b.txt

bsdiff a.txt b.txt test.patch
bspatch a.txt c.txt test.patch

diff b.txt c.txt
```

无输出 = 成功 ✅

---

## 七、常见安装问题

### ❌ 找不到 bsdiff

```bash
which bsdiff
```

若为空：

```bash
export PATH=/usr/local/bin:$PATH
```

---

### ❌ make 失败（缺 bzip2）

```bash
sudo apt install libbz2-dev
```

---


## 生成 patch

```shell
bsdiff: usage: bsdiff oldfile newfile patchfile
```

- 合成 patch

```shell
usage: bspatch oldfile newfile patchfile 
```

- eg

```shell
bsdiff old.txt lastest.txt diff.patch
```
