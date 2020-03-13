package ch.epfl.sdp.game

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag

object NFCTagHelper {
    fun intentToNFCTag(intent: Intent) = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)?.id?.toHexString()

    fun ByteArray.toHexString() = this.joinToString(""){ String.format("%02X",(it.toInt() and 0xFF)) }

    fun String.byteArrayFromHexString() = this.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}