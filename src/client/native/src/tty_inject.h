/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class client_Receiver */

#ifndef _Included_client_Receiver
#define _Included_client_Receiver
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     client_Receiver
 * Method:    open_term
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_client_Receiver_open_1term
  (JNIEnv *, jobject, jstring);

/*
 * Class:     client_Receiver
 * Method:    write_char
 * Signature: (IC)I
 */
JNIEXPORT jint JNICALL Java_client_Receiver_write_1char
  (JNIEnv *, jobject, jint, jchar);

/*
 * Class:     client_Receiver
 * Method:    close_term
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_client_Receiver_close_1term
  (JNIEnv *, jobject, jint);

#ifdef __cplusplus
}
#endif
#endif
