/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import type { Node } from 'react';
import React, { useCallback } from 'react';
import { Pressable, SafeAreaView, StyleSheet, Text } from 'react-native';
import { NativeModules } from 'react-native';
import {launchCamera, launchImageLibrary} from 'react-native-image-picker';

const { RNQrCodeScanner } = NativeModules;

function toFilePath(uri: string) {
  let filePath = uri.replace('file://', '');
  if (filePath.includes('%')) {
    filePath = decodeURIComponent(filePath);
  }
  return filePath;
}

const App: () => Node = () => {
  const handleButtonClick = useCallback(async() => {
    // await RNQrCodeScanner.scanQrCodeUrl(filePath);
    launchImageLibrary({ mediaType: 'photo' }, async response => {
      const uri = response.uri;
      if (!uri) {
        return;
      }

      const filePath = toFilePath(uri);

      try {
        const url = await RNQrCodeScanner.scanQrCodeUrl(filePath);
        console.log(url);
      } catch (err) {
        console.error(err);
      }
    });
  }, []);

  return (
    <SafeAreaView style={styles.container}>
      <Pressable style={styles.button} onPress={handleButtonClick}>
        <Text style={styles.buttonText}>Button</Text>
      </Pressable>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  button: {
    backgroundColor: 'purple',
    paddingVertical: 10,
    paddingHorizontal: 20,
  },
  buttonText: {
    color: 'white',
    fontSize: 20
  },
});

export default App;
