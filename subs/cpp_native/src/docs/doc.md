## 使用 CMakeLists.txt 的方式

### 完全源码编译

#### 源码自身包含全部内容 

- `bspatch.c` 中已经完全引入了全部源码，包含完整的头文件和 c/cpp  

```c
#include <bzlib.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <err.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include "bspatch.h"
/** 导入bzip2的引用*/
#include "bzip/bzlib.c"
#include "bzip/crctable.c"
#include "bzip/compress.c"
#include "bzip/decompress.c"
#include "bzip/randtable.c"
#include "bzip/blocksort.c"
#include "bzip/huffman.c"

```
那么，在 `CMakeLists.txt` 中只需配置头文件即可 

```cmake
include_directories(../bzip/)
include_directories(../logcat)

add_library( # Sets the name of the library.
        cpp_native

        # Sets the library as a shared library.
        SHARED
        ../bspatch.c
        # Provides a relative path to your source file(s).
        ../cpp_native.cpp)
```
`include_directories` 会导入所有需要的 .h 文件，这样在 `bspatch.c` 通过文件名就可以直接引入了,同时由于 `bspatch.c` 引入了所有源码，编译时只需要引入单个文件即可

#### 源码只包含头文件 

- `bspatch.c` 中只引入了头文件

```c
#include <bzlib.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <err.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include "bspatch.h"
/** 导入bzip2的引用*/
//#include "bzip/bzlib.c"
//#include "bzip/crctable.c"
//#include "bzip/compress.c"
//#include "bzip/decompress.c"
//#include "bzip/randtable.c"
//#include "bzip/blocksort.c"
//#include "bzip/huffman.c"
```
对于这种情况，需要在 `CMakeLists.txt` 中配置完整的源码文件

```cmake

file(GLOB BZIP ../bzip/*.c)
message("----------start------")
message("${BZIP}")
message("${CMAKE_CURRENT_SOURCE_DIR}")
message("----------end------")
#导入头文件
include_directories(../bzip/)
include_directories(../logcat)

add_library( # Sets the name of the library.
        cpp_native

        # Sets the library as a shared library.
        SHARED
        #将bzip下的.c文件添加到library
        ${BZIP}
        ../bspatch.c
        # Provides a relative path to your source file(s).
        ../cpp_native.cpp)
```

通过以上配置，{BZIP} 包含全部 .c 文件， add_library 配置之后，就包含了全部源码。但是以上两种情况不能混用，否则会产生冲突。
即如果 `bspatch.c` 如果已经有了完整源码，即不需要再通过 add_library 添加多余的内容了。

```cmake
target_link_libraries( # Specifies the target library.
        cpp_native
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
```
最终会生成 `cpp_native.so`  ,其实这个配置不是必须的，这个配置只是让 cpp_native 包含了 log-lib 的内容，如果用不到的话，可以忽略。
add_library 这个配置其实就可以确保 so 可以生成。

### 生成任意 so 

```cmake
# 将 bspatch 的源码编译为共享库
add_library(
        bspatch_tool
        SHARED
        ../bspatch.c)
```
比如按上面这样配置之后，就会同时生成一个名为 bspatch_tool 的 so 

[](libs.png)

这样就生成了两个 so, 后续我们只要有头文件或者说函数声明，就可以在任意位置方便的使用 so 中包含的方法了


### 依赖 so 及头文件编译

通过上一步，我们将 `bspatch.c` 的实现打包成了一个 so ,那么我们就可以摆脱源码，直接使用这个文件了。


```cmake

#导入头文件
include_directories(bzip/)
include_directories(logcat/)


add_library( # Sets the name of the library.
        cpp_native

        # Sets the library as a shared library.
        SHARED
       
        cpp_native.cpp)

## 定义 distributionDir 变量,声明所依赖的 so 库存放的位置，这个不是必须的，只是为了灵活方便
set(distributionDir ${CMAKE_CURRENT_SOURCE_DIR}/third-lib)[imitate-debug.apk](../../../../imitate/build/intermediates/apk/debug/imitate-debug.apk)
## 所依赖的三方库定义名称，STATIC IMPORTED 表示是导入的
add_library(lib_bspatch STATIC IMPORTED)
## 定义导入的 so 库的路径
set_target_properties(lib_bspatch PROPERTIES IMPORTED_LOCATION ${distributionDir}/${ANDROID_ABI}/libbspatch_tool.so)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        cpp_native
        lib_bspatch
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
```

我们只需要导入头文件，并且将 libbspatch_tool.so 设置为一个导入的静态库，最后只需要进行链接即可，这样最终生成的产物中，会有 cpp_native,lib_bspatch 两个 so 文件，使用起来更加的方便。