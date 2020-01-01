# ZX81 Frogging

My first computer was a [ZX81](https://en.wikipedia.org/wiki/ZX81), and the first game I wrote
was called "Frogging", where you have to catch a frog on a lily pad that you can move
left or right.

## Web emulator

The emulator in _web_ uses the code for [JtyOne Online ZX81 Emulator](http://www.zx81stuff.org.uk/zx81/jtyone.html),
as modified by [www.perfectlynormalsite.com/helloworld.html](www.perfectlynormalsite.com/helloworld.html).

I made a small change so it can load _.p_ files, although it expects the _.hex_ version,
which can be generated like so:

```bash
xxd -p frogging-reconstruction.p | tr -d '\n' > web/images/frogging.p.hex
```

To try out locally, start a webserver:

```bash
(cd web; python -m SimpleHTTPServer 8000)
```

and go to [http://localhost:8000/frogging.html](http://localhost:8000/frogging.html).

Type R followed by return to run the program, and use the 5 and 8 keys to move left and right.

It's not the best game ever, but it's the first program I ever wrote (age 9), so it's not surprising.

## Run locally

```bash
~/sw/zxtext2p/zxtext2p -o frogging-reconstruction.p frogging-reconstruction.bas
```

Then open the _.p_ file using [zxsp](https://www.macupdate.com/app/mac/24529/zxsp).
