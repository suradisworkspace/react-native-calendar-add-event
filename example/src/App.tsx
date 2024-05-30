import * as React from 'react';

import { StyleSheet, View, Text, TouchableOpacity } from 'react-native';
import CalendarEvent from 'react-native-calendar-add-event';

type ButtonPropsTypes = {
  title: string;
  onPress: () => void;
};

const Button = (props: ButtonPropsTypes) => {
  return (
    <TouchableOpacity style={styles.btn} onPress={props.onPress}>
      <Text>{props.title}</Text>
    </TouchableOpacity>
  );
};

export default function App() {
  const addEvent = async () => {
    const startTime = new Date(Date.now() + 3600 * 1000);
    const endTime = new Date(Date.now() + 7200 * 1000);
    try {
      // const res = await CalendarEvent.addEvent(
      //   'some event',
      //   startTime,
      //   endTime
      // );
      const res = await CalendarEvent.addEvent({
        title: 'some event',
        startDate: startTime,
        endDate: endTime,
      });
      console.warn('res', res);
    } catch (err) {
      console.warn('err', err);
    }
  };

  return (
    <View style={styles.container}>
      <Button title="add to  calendar" onPress={addEvent} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
  btn: {
    width: 300,
    height: 50,
    backgroundColor: 'green',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 15,
  },
});
