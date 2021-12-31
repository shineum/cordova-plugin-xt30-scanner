# cordova-plugin-xt30-scanner
Cordova Beam Scanner for Point Mobile XT30

# Install

```javascript

cordova plugin add cordova-plugin-xt30-scanner

```

# Usages

### 1. Scan

```javascript
cordova.plugins.xt30scanner.scan(successCallback, errorCallback);
// then callbacks will be set and scanner will be ready.

var successCallback = function(result, metadata) {
  // metadata.type
  console.log(result, metadata);  
}

var errorCallback = function(error) {
  console.log(error);
}
```


```javascript
cordova.plugins.xt30scanner.scan();
// if you already set callbacks, calling scan() without parameters will work also.

```

### 2. Cancel

```javascript
cordova.plugins.xt30scanner.cancel();
// then the scanner is off and error callback will be fired with error message of "USER_CANCEL".

```

### 3. Beep

```javascript
cordova.plugins.xt30scanner.beep(mode);
// to control beep when the scanner reads the codes. mode : true or false

```

### 4. Set Callbacks

```javascript
cordova.plugins.xt30scanner.set(successCallback, errorCallback);
// Setting callback functions to read code with side buttons.

```

### 5. Set Trigger Mode

```javascript
cordova.plugins.xt30scanner.setTriggerMode(mode);
// Set scanner trigger mode.

// Available modes are:
// cordova.plugins.xt30scanner.TRIGGER_MODE.PULSE
// cordova.plugins.xt30scanner.TRIGGER_MODE.CONTINUOUS
// cordova.plugins.xt30scanner.TRIGGER_MODE.ONESHOT

```

# history

### 0.0.2
- Add document

### 0.0.1
- Initial publish

# License

MIT
