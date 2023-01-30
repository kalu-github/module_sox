#
#### 注意
```
仅支持wav音频格式
```

#
#### 语音降噪(更改0.21以调整采样率的灵敏度级别[0.2-0.3通常提供最佳结果])
```
sox tmpaud.wav -n noiseprof noise.prof
sox tmpaud.wav tmpaud-clean.wav noisered noise.prof 0.21
```

# 
#### 通过截取音频中的已知噪音部分，根据该噪音样本对整个音频进行降噪。截取噪音使用ffmpeg，降噪使用sox
```
1. 将音频流和视频流拆分为2个不同的文件:
视频: ffmpeg -i input.mp4 -vcodec copy -an tmpvid.mp4
音频: ffmpeg -i input.mp4 -acodec pcm_s16le -ar 128k -vn tmpaud.wav
2. 从上一步的音频结果文件中剪切一个噪声样本:
ffmpeg -i itmpaud.wav -ss 00:00:00.0 -t 00:00:00.5 noiseaud.wav
-ss: 从开始的时间偏移. (h: m: s.ms).
-t duration: 表示要剪切的音频段的持续时间（h: m: s.ms），以便下一步用来作为噪声文件。
选择一段没有语音、只有噪音的音频（例如，讲话者静音时的那一秒钟）。
3. 使用sox生成噪音profile:
sox noiseaud.wav -n noiseprof noise.prof
4. 清除音频流中的噪声样本：
sox tmpaud.wav tmpaud-clean.wav noisered noise.prof 0.21
更改0.21以调整采样率的灵敏度级别（0.2-0.3通常提供最佳结果）。
5. 使用ffmpeg将新的音频和视频流合并到一起:
ffmpeg -i tmpvid.mp4 -i tmpaud-clean.wav -map 0:v -map 1:a -c:v copy -c:a aac -b:a 128k out.mp4
```