# ZX81 Frogging

My first computer was a [ZX81](https://en.wikipedia.org/wiki/ZX81), and the first game I wrote
was called "Frogging", where you have to catch a frog on a lily pad that you can move
left or right.

[Try it in a browser](https://tomwhite.github.io/zx81-frogging/web/frogging.html). Type type R followed by ENTER, and use the 5 and 8 keys to move left and right.

It's not the best game ever, but it's the first program I ever wrote (age 9), so it's not surprising!

## How it works

The programs are saved in text format with a `.bas` file extension. Graphics characters and inverse video characters are represented using the conventions described in [ZXText2P](http://freestuff.grok.co.uk/zxtext2p/index.html). The sequence `\::`, for example, represents a black square.

The `zxtext2p` command was used to convert these text files to ZX81 P files suitable for use in an emulator, or a real ZX81. I used a [modified version](https://github.com/tomwhite/zxtext2p) of ZXText2P that collapses the display file, so it will fit on a 1K ZX81.

The emulator in _web_ uses the code for [JtyOne Online ZX81 Emulator](http://www.zx81stuff.org.uk/zx81/jtyone.html),
as modified by [www.perfectlynormalsite.com/helloworld.html](www.perfectlynormalsite.com/helloworld.html).

I made a small change so it can load _.p_ files, although it expects the _.hex_ version. The full build is achieved with:

```bash
zxtext2p -d -o frogging-reconstruction.p frogging-reconstruction.bas
xxd -p frogging-reconstruction.p | tr -d '\n' > web/images/frogging.p.hex
```

To try out locally, start a webserver:

```bash
(cd web; python -m http.server 8000)
```

and go to [http://localhost:8000/frogging.html](http://localhost:8000/frogging.html).

Type R followed by ENTER to run the program, and use the 5 and 8 keys to move left and right.

You can also run the _.p_ file in an emulator, for example using [zxsp](https://www.macupdate.com/app/mac/24529/zxsp).
