#!/usr/bin/env bash
# ------------- 1. 清端口 -----------------------------
for port in 3306 6379 5672 15672; do
  echo ">>> 检查并释放端口 $port ..."
  # 找到监听该端口的 PID
  pid=$(sudo lsof -t -i :$port 2>/dev/null)
  if [[ -n $pid ]]; then
    echo "发现占用：PID=$pid"
    # 如果是容器进程（/proc/$PID/root 存在且指向 docker 文件系统）
    if [[ -L /proc/$pid/exe && $(readlink /proc/$pid/exe) == *"/docker/"* ]]; then
      echo "是容器进程，跳过（稍后 docker compose up 会复用或重建）"
    else
      echo "杀掉宿主机进程 $pid"
      sudo kill -9 $pid
    fi
  else
    echo "端口 $port 已空闲"
  fi
done

# ------------- 2. 启服务 -----------------------------
echo ">>> 启动所有服务 ..."
docker compose up -d

# ------------- 3. 刷日志 -----------------------------
echo ">>> 实时日志（Ctrl-C 退出）..."
docker compose logs -f


# echo "→ 已启动，浏览器打开 http://localhost:8080"
