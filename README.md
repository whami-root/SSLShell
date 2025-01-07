# SSLShell

基于 Java 原生Socket和SSL/TLS 加密的反向 Shell 代码，支持代码执行和反序列化漏洞利用场景。

## 使用方法

### 服务端监听

```
ncat -nlvp 1337 --ssl
```

流量完全加密

![image-20230630112429671](https://github.com/whami-root/SSLShell/assets/63653652/b6eb0ba1-b689-4873-9b36-1329253783dd)


### 利用场景

1. 直接代码执行
   - 直接加载并执行tlsShell5.java
   
2. 反序列化利用
   - 使用ysoserial加载tlsshellTemplate.java，只考虑了TemplatesImpl类的payload
   - 生成包含加密反向 Shell 的 payload

## 技术特点

- 使用 Java 内置 SSLSocket
- 无需额外依赖
- 通信过程全程加密
