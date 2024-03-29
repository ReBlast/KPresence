package io.github.reblast.kpresence.ipc

import io.github.reblast.kpresence.utils.putInt
import io.github.reblast.kpresence.utils.reverseBytes
import kotlinx.cinterop.*
import platform.posix.*
import platform.windows.*

actual fun openPipe(): Int {
  for (i in 0..9) {
    val handle = open("\\\\.\\pipe\\discord-ipc-$i", O_RDWR)
    
    if (handle == -1) continue
    else return handle
  }
  
  throw RuntimeException("Could not connect to the pipe!")
}

actual fun closePipe(handle: Int) {
  close(handle)
}

actual fun readBytes(handle: Int, bufferSize: Int): ByteArray {
  if (handle == -1) throw IllegalStateException("Not connected")
  
  val buffer = ByteArray(bufferSize)
  val bytesRead = read(handle, buffer.refTo(0), buffer.size.toUInt())
  
  return buffer.copyOf(bytesRead)
}

actual fun writeBytes(handle: Int, opcode: Int, data: String) {
  if (handle == -1) throw IllegalStateException("Not connected")

  val bytes = data.encodeToByteArray()
  val buffer = ByteArray(bytes.size + 8)

  buffer.putInt(opcode.reverseBytes())
  buffer.putInt(bytes.size.reverseBytes(), 4)
  bytes.copyInto(buffer, 8)

  write(handle, buffer.refTo(0), buffer.size.toUInt())
}
