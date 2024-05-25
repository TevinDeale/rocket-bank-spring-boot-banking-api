#! bin/bash
yum update -y
yum install -y docker
systemctl enable docker
systemctl start docker
docker login -u tevindeale -p <pw>
docker run -itd --network host --name rbapi --restart always tevindeale/rocketbankapi:amd64-1.0.8