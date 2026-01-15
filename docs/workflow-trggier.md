## workflow 配置

在 **GitHub 上的开源 Android 项目**中，通过 **GitHub Actions 自动生成可供用户下载的产物（APK / AAB / ZIP 等）**，是非常常见、也非常规范的做法。下面我按 **“推荐实践 → 具体配置 → 常见进阶方案”** 给你一个**完整可落地方案**。

---

## 一、总体思路（你要做的 3 件事）

1. **用 GitHub Actions 触发 CI**

    * push / PR / tag / 手动触发
2. **在 CI 中编译 Android 项目**

    * Gradle + JDK + Android SDK
3. **把编译产物上传为可下载资源**

    * Artifacts（临时）
    * Releases（长期、面向用户）

---

## 二、最常用方案（强烈推荐）：Release + APK/AAB

> **用户体验最好、最标准**

### 效果

* 用户进入 GitHub → Releases
* 直接下载 APK / AAB
* 每个版本都有历史记录

---

## 三、基础 GitHub Actions 工作流示例

### 1️⃣ 创建 workflow 文件

```bash
.github/workflows/android-release.yml
```

---

### 2️⃣ 最小可用示例（APK）

```yaml
name: Android Release Build

on:
  push:
    tags:
      - "v*"
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Setup Android SDK
        uses: android-actions/setup-android@v3

      - name: Grant execute permission
        run: chmod +x gradlew

      - name: Build Release APK
        run: ./gradlew assembleRelease

      - name: Upload APK to GitHub Release
        uses: softprops/action-gh-release@v2
        with:
          files: app/build/outputs/apk/release/*.apk
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

---

### 3️⃣ 使用方式

```bash
git tag v1.0.0
git push origin v1.0.0
```

👉 自动生成 Release + APK

---

## 四、如果你只是想「临时下载」（Artifacts）

> 适合：PR 验证 / 测试包

```yaml
- name: Upload Artifact
  uses: actions/upload-artifact@v4
  with:
    name: app-release-apk
    path: app/build/outputs/apk/release/*.apk
```

📌 特点：

* 保留 90 天
* 登录 GitHub 才能下载
* 不适合普通用户

---

## 五、AAB / 多 Variant 支持

### AAB

```yaml
- name: Build AAB
  run: ./gradlew bundleRelease
```

```yaml
files: app/build/outputs/bundle/release/*.aab
```

---

### 多 flavor / buildType

```bash
./gradlew assembleProdRelease
./gradlew assembleChinaRelease
```

```yaml
files: |
  app/build/outputs/apk/*/release/*.apk
```

---

## 六、签名 APK（真实项目必做）

### 1️⃣ 把 keystore 转为 Base64

```bash
base64 my-release.keystore > keystore.b64
```

### 2️⃣ 存入 GitHub Secrets

| Secret 名          | 内容           |
| ----------------- | ------------ |
| KEYSTORE_BASE64   | keystore.b64 |
| KEYSTORE_PASSWORD | xxx          |
| KEY_ALIAS         | xxx          |
| KEY_PASSWORD      | xxx          |

---

### 3️⃣ Action 中恢复 keystore

```yaml
- name: Decode keystore
  run: |
    echo "${{ secrets.KEYSTORE_BASE64 }}" | base64 -d > keystore.jks
```

---

### 4️⃣ Gradle signingConfig（示例）

```gradle
signingConfigs {
    release {
        storeFile file("../keystore.jks")
        storePassword System.getenv("KEYSTORE_PASSWORD")
        keyAlias System.getenv("KEY_ALIAS")
        keyPassword System.getenv("KEY_PASSWORD")
    }
}
```

---

## 七、开源项目常见进阶玩法

### ✅ 自动生成 Changelog

* github-release-notes
* conventional commits

### ✅ PR 自动生成 Debug APK

* PR → Artifact → 测试人员下载

### ✅ Nightly 构建

```yaml
on:
  schedule:
    - cron: "0 0 * * *"
```

### ✅ 同时发布到：

* GitHub Releases
* Telegram Bot
* OSS / COS / S3

---

## 八、一个「规范开源 Android 项目」的 CI 结构

```
.github/
 └─ workflows/
    ├─ android-pr.yml        # PR 校验
    ├─ android-debug.yml     # Debug APK
    └─ android-release.yml   # 正式发布
```

---

## 九、你可以直接照抄的 Checklist

* [ ] 有 workflow
* [ ] 能 assembleRelease
* [ ] 能上传 artifact / release
* [ ] 有 tag 触发
* [ ] keystore 不进仓库
* [ ] Release 页面有 APK

---

## 十、如果你愿意，我可以继续帮你

你可以直接告诉我：

1. **你现在用的是**

    * Groovy / Kotlin DSL？
    * AGP 版本？
2. **产物类型**

    * APK / AAB？
    * 是否多 flavor？
3. **是否需要签名**

👉 我可以直接帮你 **定制一份完整可用的 Action 文件**（可直接复制）


## 如何限制 workflow 触发，除了 push ，还有哪些更精细的实现方式

这是一个 **GitHub Actions 非常关键、但经常被忽略的点** 👍
下面我从 **「触发条件 → 触发范围 → 触发权限 → 触发时机」** 四个维度，系统性讲 **除了 `push` 以外，如何“精细化”限制 workflow 触发**，并配完整可用示例。

---

# 一、最常用的触发方式总览（速览表）

| 维度 | 方式                             | 典型用途              |
| -- | ------------------------------ | ----------------- |
| 分支 | `branches` / `branches-ignore` | 只在 main / release |
| 路径 | `paths` / `paths-ignore`       | 只改 Android 才跑     |
| PR | `pull_request`                 | 只在 PR 校验          |
| 标签 | `tags`                         | 只发版               |
| 手动 | `workflow_dispatch`            | 人工触发              |
| 定时 | `schedule`                     | Nightly           |
| 事件 | `release` / `workflow_run`     | 级联                |
| 权限 | `permissions`                  | 最小权限              |
| 条件 | `if:`                          | 运行时判断             |

---

# 二、分支级别限制（最基础）

### 只在 `main` / `release` 触发

```yaml
on:
  push:
    branches:
      - main
      - release/*
```

### 排除分支

```yaml
on:
  push:
    branches-ignore:
      - dev
      - experimental/**
```

📌 **发版 workflow 一定要限制分支**

---

# 三、路径级别限制（Android 项目非常重要）

> 只要不是 Android 相关改动，就不跑 CI

```yaml
on:
  push:
    paths:
      - "app/**"
      - "gradle/**"
      - "*.gradle"
      - "*.gradle.kts"
```

### 排除文档 / README

```yaml
on:
  push:
    paths-ignore:
      - "**/*.md"
      - "docs/**"
```

💡 **极大节省 CI 时间**

---

# 四、PR 触发（推荐用于校验）

```yaml
on:
  pull_request:
    branches:
      - main
```

### 只在 PR 打开 / 更新时触发

```yaml
on:
  pull_request:
    types: [opened, synchronize, reopened]
```

📌 **PR 校验 ≠ Release 构建**

---

# 五、Tag 触发（发布专用，最常见）

```yaml
on:
  push:
    tags:
      - "v*"
```

### 结合分支限制（更安全）

```yaml
on:
  push:
    tags:
      - "v*"
    branches:
      - main
```

👉 **避免从错误分支打 tag**

---

# 六、手动触发（最灵活）

```yaml
on:
  workflow_dispatch:
```

### 带参数（非常实用）

```yaml
on:
  workflow_dispatch:
    inputs:
      buildType:
        description: "Build type"
        required: true
        default: "release"
```

```yaml
- run: ./gradlew assemble${{ inputs.buildType | capitalize }}
```

📌 **用于紧急包 / 测试包**

---

# 七、定时任务（Nightly / Weekly）

```yaml
on:
  schedule:
    - cron: "0 2 * * *"
```

> UTC 时间！

### 限制只在 main 跑

```yaml
jobs:
  build:
    if: github.ref == 'refs/heads/main'
```

---

# 八、Release 事件触发（专业做法）

> **只有真正创建 Release 才跑**

```yaml
on:
  release:
    types: [published]
```

📌 适合：

* 自动上传 APK
* 同步 OSS / Telegram

---

# 九、workflow 级联（进阶）

### PR → Build → Release

```yaml
on:
  workflow_run:
    workflows: ["Android PR Check"]
    types:
      - completed
```

```yaml
if: ${{ github.event.workflow_run.conclusion == 'success' }}
```

📌 **强制“先绿再发版”**

---

# 十、运行时条件（if，比 on 更细）

### 限制仓库 owner

```yaml
if: github.repository_owner == 'your-org'
```

### 限制 Actor（防止 fork 滥用）

```yaml
if: github.actor == 'trusted-bot'
```

### 限制 Commit Message

```yaml
if: contains(github.event.head_commit.message, '[release]')
```

---

# 十一、Fork / 外部 PR 安全限制（非常重要）

### 防止 fork PR 访问 secrets

```yaml
on:
  pull_request:
```

⚠️ `pull_request` **默认无 secrets**

### 如果一定要用 secrets（谨慎）

```yaml
on:
  pull_request_target:
```

📌 **仅用于 read-only 操作**

---

# 十二、权限级别限制（最小权限原则）

```yaml
permissions:
  contents: read
  actions: none
```

### 发布 Release 才给写权限

```yaml
permissions:
  contents: write
```

---

# 十三、一个「强约束 Release Workflow」示例（推荐）

```yaml
on:
  push:
    tags:
      - "v*"

permissions:
  contents: write

jobs:
  release:
    if: github.ref_type == 'tag' &&
        startsWith(github.ref_name, 'v')
    runs-on: ubuntu-latest
```

✔ 只能 tag
✔ 只能 v*
✔ 只能写 release

---

# 十四、设计建议（经验总结）

### 🔒 Release 构建

* `push tag`
* `main` 分支
* `permissions: write`
* 手动 + tag 双保险

### ⚡ PR 构建

* `pull_request`
* `paths`
* 无 secrets

### 🧪 Debug 构建

* `workflow_dispatch`
* 带参数

---

## 如果你愿意，我可以：

* 🔧 **帮你设计一套完整的 workflow 拆分方案**
* 🧩 **基于你当前项目结构直接写 YAML**
* 🔐 **帮你审计现有 workflow 的安全问题**

你可以贴一个：

* `.github/workflows/*.yml`
* 或项目目录结构
