#### io nio
io中核心概念是流Stream，面向流编程，在java io中一个流要么是输入流，要么就是输出流，两者不可能同时存在。

nio中有三个核心概念。Selector选择器，Channel通道和Buffer缓冲区。在nio中，是面向块(block)或是缓冲区(buffer)编程的，Buffer本身即是一块内存，在底层实现上，是一个数据，数据的读写都是通过Buffer实现的。

java中的8中原生数据类型都有对应的BUffer类型，如IntBuffer，LongBuffer等。

Channel指的是可以向其写入数据或是从中读取数据的对象，类似于io中的Stram

所有的数据都是通过Buffer来完成的，永远不会出现直接向Channel中直接操作数据。与Stream不同的是，Channel是双向的，可以对Buffer进行读取、写入、或者是读写都可以，但是io的Stream只能读或者写。

由于Channel是双向的，因此它能更好的反应出操作系统的真实情况，如在Linux中，系统的通道就是双向的。

相关代码如下:

```java

```