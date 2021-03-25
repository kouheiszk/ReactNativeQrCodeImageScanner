/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import type { Node } from "react";
import React, { useCallback } from "react";
import { Pressable, SafeAreaView, StyleSheet, Text } from "react-native";

const App: () => Node = () => {
  const handleButtonClick = useCallback(() => {
    console.log('hoge');
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
