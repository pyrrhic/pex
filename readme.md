Just some thoughts I had while making this:

I figure the 3 biggest things affecting CPU & RAM are:
Download speed
Interaction with HD (reading file input, writing to output)
Image processing

Multithreading could increase throughput of the download and image processing steps.

Not sure if it's possible due to compressed images, but depending on image format, could stream the images instead of
loading them entirely into RAM.

