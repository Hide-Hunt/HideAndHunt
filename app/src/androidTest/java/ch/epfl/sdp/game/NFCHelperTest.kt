package ch.epfl.sdp.game

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Parcelable
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.NFCTestHelper
import ch.epfl.sdp.game.NFCTagHelper.toHexString
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalUnsignedTypes
@RunWith(AndroidJUnit4::class)
class NFCHelperTest {
    @Test
    fun tagToStringReturnsCorrectValues() {
        val bytes = ByteArray(2)
        bytes[0] = 0xFF.toByte()
        bytes[1] = 0xCA.toByte()
        assertEquals("FFCA", bytes.toHexString())
    }

    @Test
    fun intentWithoutTagShouldReturnNull() {
        val intent = Intent()
        val tag = NFCTagHelper.intentToNFCTag(intent)
        assertEquals(null, tag)
    }

    @ExperimentalStdlibApi
    @Test
    fun intentWithTagShouldReturnTheTag() {
        val bytes = ByteArray(2)
        bytes[0] = 0xFF.toByte()
        bytes[1] = 0xCA.toByte()

        val baseTag = NFCTestHelper.createMockTag(bytes)

        val intent = Intent()
        intent.putExtra(NfcAdapter.EXTRA_TAG, baseTag as Parcelable)
        val tag = NFCTagHelper.intentToNFCTag(intent)
        assertEquals("FFCA", tag)
    }
}