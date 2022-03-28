http://ffmpeg.org/download.html
http://note.rpsh.net/posts/2015/04/21/mac-osx-javacv-mp4-gif-convert/

```shell
srcFile="/Users/hocgin/Downloads/xx.mp4"

# 从 00:02:15 截取 20s
./ffmpeg -i $srcFile -ss 00:02:15 -codec copy -t 20 output.mp4

# 从 00:02:15 截取 15min
./ffmpeg -i $srcFile -ss 00:02:15 -t 00:15:00 -codec copy output.mp4

/Users/hocgin/Projects/spring-boot-starters-project/spring-boot-ffmpeg/javacv-spring-boot-autoconfigure/src/main/resources/app/macos/ffmpeg -i "./一念永恒(第一季.01).mp4" -i "./一念永恒(第一季.02).mp4" -c:v copy -c:a aac -strict experimental mvresult.mp4

```





