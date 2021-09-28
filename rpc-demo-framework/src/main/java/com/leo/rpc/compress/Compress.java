package com.leo.rpc.compress;

import com.leo.rpc.extension.SPI;

@SPI
public interface Compress {

	byte[] compress(byte[] bytes);

	byte[] decompress(byte[] bytes);
}
