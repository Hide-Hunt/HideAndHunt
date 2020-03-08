package ch.epfl.sdp

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle

object NFCTestHelper {
    // Based on https://stackoverflow.com/questions/30841803/how-to-mock-a-android-nfc-tag-object-for-unit-testing
    fun createMockTag(id: ByteArray): Tag {
        val tagClass: Class<*> = Tag::class.java
        val createMockTagMethod = tagClass.getMethod("createMockTag", ByteArray::class.java, IntArray::class.java, Array<Bundle>::class.java)

        val nfcaBundle = Bundle()
        nfcaBundle.putByteArray(EXTRA_NFC_A_ATQA, arrayOf(0x44.toByte(), 0x00.toByte()).toByteArray()) //ATQA for Type 2 tag
        nfcaBundle.putShort(EXTRA_NFC_A_SAK, 0x00.toShort()) //SAK for Type 2 tag

        return createMockTagMethod.invoke(null,
                id,
                intArrayOf(TECH_NFC_A),
                arrayOf(nfcaBundle)) as Tag
    }

    fun createTechDiscovered(tag: Tag) : Intent {
        val intent = Intent(NfcAdapter.ACTION_TAG_DISCOVERED)
        intent.putExtra(NfcAdapter.EXTRA_ID, tag.id)
        intent.putExtra(NfcAdapter.EXTRA_TAG, tag)
        return intent
    }

    const val TECH_NFC_A = 1
    const val EXTRA_NFC_A_SAK = "sak" // short (SAK byte value)

    const val EXTRA_NFC_A_ATQA = "atqa" // byte[2] (ATQA value)


    const val TECH_NFC_B = 2
    const val EXTRA_NFC_B_APPDATA = "appdata" // byte[] (Application Data bytes from ATQB/SENSB_RES)

    const val EXTRA_NFC_B_PROTINFO = "protinfo" // byte[] (Protocol Info bytes from ATQB/SENSB_RES)


    const val TECH_ISO_DEP = 3
    const val EXTRA_ISO_DEP_HI_LAYER_RESP = "hiresp" // byte[] (null for NfcA)

    const val EXTRA_ISO_DEP_HIST_BYTES = "histbytes" // byte[] (null for NfcB)


    const val TECH_NFC_F = 4
    const val EXTRA_NFC_F_SC = "systemcode" // byte[] (system code)

    const val EXTRA_NFC_F_PMM = "pmm" // byte[] (manufacturer bytes)


    const val TECH_NFC_V = 5
    const val EXTRA_NFC_V_RESP_FLAGS = "respflags" // byte (Response Flag)

    const val EXTRA_NFC_V_DSFID = "dsfid" // byte (DSF ID)


    const val TECH_NDEF = 6
    const val EXTRA_NDEF_MSG = "ndefmsg" // NdefMessage (Parcelable)

    const val EXTRA_NDEF_MAXLENGTH = "ndefmaxlength" // int (result for getMaxSize())

    const val EXTRA_NDEF_CARDSTATE = "ndefcardstate" // int (1: read-only, 2: read/write, 3: unknown)

    const val EXTRA_NDEF_TYPE = "ndeftype" // int (1: T1T, 2: T2T, 3: T3T, 4: T4T, 101: MF Classic, 102: ICODE)


    const val TECH_NDEF_FORMATABLE = 7

    const val TECH_MIFARE_CLASSIC = 8

    const val TECH_MIFARE_ULTRALIGHT = 9
    const val EXTRA_MIFARE_ULTRALIGHT_IS_UL_C = "isulc" // boolean (true: Ultralight C)


    const val TECH_NFC_BARCODE = 10
    const val EXTRA_NFC_BARCODE_BARCODE_TYPE = "barcodetype" // int (1: Kovio/ThinFilm)
}