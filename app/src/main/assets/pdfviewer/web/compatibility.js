'use strict';
if (typeof PDFJS === 'undefined') {
  (typeof window !== 'undefined' ? window : this).PDFJS = {};
}
(function checkTypedArrayCompatibility() {
  if (typeof Uint8Array !== 'undefined') {
    if (typeof Uint8Array.prototype.subarray === 'undefined') {
        Uint8Array.prototype.subarray = function subarray(start, end) {
          return new Uint8Array(this.slice(start, end));
        };
        Float32Array.prototype.subarray = function subarray(start, end) {
          return new Float32Array(this.slice(start, end));
        };
    }
    if (typeof Float64Array === 'undefined') {
      window.Float64Array = Float32Array;
    }
    return;
  }
  function subarray(start, end) {
    return new TypedArray(this.slice(start, end));
  }
  function setArrayOffset(array, offset) {
    if (arguments.length < 2) {
      offset = 0;
    }
    for (var i = 0, n = array.length; i < n; ++i, ++offset) {
      this[offset] = array[i] & 0xFF;
    }
  }
  function TypedArray(arg1) {
    var result, i, n;
    if (typeof arg1 === 'number') {
      result = [];
      for (i = 0; i < arg1; ++i) {
        result[i] = 0;
      }
    } else if ('slice' in arg1) {
      result = arg1.slice(0);
    } else {
      result = [];
      for (i = 0, n = arg1.length; i < n; ++i) {
        result[i] = arg1[i];
      }
    }
    result.subarray = subarray;
    result.buffer = result;
    result.byteLength = result.length;
    result.set = setArrayOffset;
    if (typeof arg1 === 'object' && arg1.buffer) {
      result.buffer = arg1.buffer;
    }
    return result;
  }
  window.Uint8Array = TypedArray;
  window.Int8Array = TypedArray;
  window.Uint32Array = TypedArray;
  window.Int32Array = TypedArray;
  window.Uint16Array = TypedArray;
  window.Float32Array = TypedArray;
  window.Float64Array = TypedArray;
})();
(function normalizeURLObject() {
  if (!window.URL) {
    window.URL = window.webkitURL;
  }
})();
(function checkObjectDefinePropertyCompatibility() {
  if (typeof Object.defineProperty !== 'undefined') {
    var definePropertyPossible = true;
    try {
      Object.defineProperty(new Image(), 'id', { value: 'test' });
      var Test = function Test() {};
      Test.prototype = { get id() { } };
      Object.defineProperty(new Test(), 'id',
        { value: '', configurable: true, enumerable: true, writable: false });
    } catch (e) {
      definePropertyPossible = false;
    }
    if (definePropertyPossible) {
      return;
    }
  }
  Object.defineProperty = function objectDefineProperty(obj, name, def) {
    delete obj[name];
    if ('get' in def) {
      obj.__defineGetter__(name, def['get']);
    }
    if ('set' in def) {
      obj.__defineSetter__(name, def['set']);
    }
    if ('value' in def) {
      obj.__defineSetter__(name, function objectDefinePropertySetter(value) {
        this.__defineGetter__(name, function objectDefinePropertyGetter() {
          return value;
        });
        return value;
      });
      obj[name] = def.value;
    }
  };
})();
(function checkXMLHttpRequestResponseCompatibility() {
  var xhrPrototype = XMLHttpRequest.prototype;
  var xhr = new XMLHttpRequest();
  if (!('overrideMimeType' in xhr)) {
    Object.defineProperty(xhrPrototype, 'overrideMimeType', {
      value: function xmlHttpRequestOverrideMimeType(mimeType) {}
    });
  }
  if ('responseType' in xhr) {
    return;
  }
  PDFJS.disableWorker = true;
  Object.defineProperty(xhrPrototype, 'responseType', {
    get: function xmlHttpRequestGetResponseType() {
      return this._responseType || 'text';
    },
    set: function xmlHttpRequestSetResponseType(value) {
      if (value === 'text' || value === 'arraybuffer') {
        this._responseType = value;
        if (value === 'arraybuffer' &&
            typeof this.overrideMimeType === 'function') {
          this.overrideMimeType('text/plain; charset=x-user-defined');
        }
      }
    }
  });
  if (typeof VBArray !== 'undefined') {
    Object.defineProperty(xhrPrototype, 'response', {
      get: function xmlHttpRequestResponseGet() {
        if (this.responseType === 'arraybuffer') {
          return new Uint8Array(new VBArray(this.responseBody).toArray());
        } else {
          return this.responseText;
        }
      }
    });
    return;
  }
  Object.defineProperty(xhrPrototype, 'response', {
    get: function xmlHttpRequestResponseGet() {
      if (this.responseType !== 'arraybuffer') {
        return this.responseText;
      }
      var text = this.responseText;
      var i, n = text.length;
      var result = new Uint8Array(n);
      for (i = 0; i < n; ++i) {
        result[i] = text.charCodeAt(i) & 0xFF;
      }
      return result.buffer;
    }
  });
})();
(function checkWindowBtoaCompatibility() {
  if ('btoa' in window) {
    return;
  }
  var digits =
    'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
  window.btoa = function windowBtoa(chars) {
    var buffer = '';
    var i, n;
    for (i = 0, n = chars.length; i < n; i += 3) {
      var b1 = chars.charCodeAt(i) & 0xFF;
      var b2 = chars.charCodeAt(i + 1) & 0xFF;
      var b3 = chars.charCodeAt(i + 2) & 0xFF;
      var d1 = b1 >> 2, d2 = ((b1 & 3) << 4) | (b2 >> 4);
      var d3 = i + 1 < n ? ((b2 & 0xF) << 2) | (b3 >> 6) : 64;
      var d4 = i + 2 < n ? (b3 & 0x3F) : 64;
      buffer += (digits.charAt(d1) + digits.charAt(d2) +
                 digits.charAt(d3) + digits.charAt(d4));
    }
    return buffer;
  };
})();
(function checkWindowAtobCompatibility() {
  if ('atob' in window) {
    return;
  }
  var digits =
    'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
  window.atob = function (input) {
    input = input.replace(/=+$/, '');
    if (input.length % 4 === 1) {
      throw new Error('bad atob input');
    }
    for (
      var bc = 0, bs, buffer, idx = 0, output = '';
      buffer = input.charAt(idx++);
      ~buffer && (bs = bc % 4 ? bs * 64 + buffer : buffer,
        bc++ % 4) ? output += String.fromCharCode(255 & bs >> (-2 * bc & 6)) : 0
    ) {
      buffer = digits.indexOf(buffer);
    }
    return output;
  };
})();
(function checkFunctionPrototypeBindCompatibility() {
  if (typeof Function.prototype.bind !== 'undefined') {
    return;
  }
  Function.prototype.bind = function functionPrototypeBind(obj) {
    var fn = this, headArgs = Array.prototype.slice.call(arguments, 1);
    var bound = function functionPrototypeBindBound() {
      var args = headArgs.concat(Array.prototype.slice.call(arguments));
      return fn.apply(obj, args);
    };
    return bound;
  };
})();
(function checkDatasetProperty() {
  var div = document.createElement('div');
  if ('dataset' in div) {
    return;
  }
  Object.defineProperty(HTMLElement.prototype, 'dataset', {
    get: function() {
      if (this._dataset) {
        return this._dataset;
      }
      var dataset = {};
      for (var j = 0, jj = this.attributes.length; j < jj; j++) {
        var attribute = this.attributes[j];
        if (attribute.name.substring(0, 5) !== 'data-') {
          continue;
        }
        var key = attribute.name.substring(5).replace(/\-([a-z])/g,
          function(all, ch) {
            return ch.toUpperCase();
          });
        dataset[key] = attribute.value;
      }
      Object.defineProperty(this, '_dataset', {
        value: dataset,
        writable: false,
        enumerable: false
      });
      return dataset;
    },
    enumerable: true
  });
})();
(function checkClassListProperty() {
  var div = document.createElement('div');
  if ('classList' in div) {
    return;
  }
  function changeList(element, itemName, add, remove) {
    var s = element.className || '';
    var list = s.split(/\s+/g);
    if (list[0] === '') {
      list.shift();
    }
    var index = list.indexOf(itemName);
    if (index < 0 && add) {
      list.push(itemName);
    }
    if (index >= 0 && remove) {
      list.splice(index, 1);
    }
    element.className = list.join(' ');
    return (index >= 0);
  }
  var classListPrototype = {
    add: function(name) {
      changeList(this.element, name, true, false);
    },
    contains: function(name) {
      return changeList(this.element, name, false, false);
    },
    remove: function(name) {
      changeList(this.element, name, false, true);
    },
    toggle: function(name) {
      changeList(this.element, name, true, true);
    }
  };
  Object.defineProperty(HTMLElement.prototype, 'classList', {
    get: function() {
      if (this._classList) {
        return this._classList;
      }
      var classList = Object.create(classListPrototype, {
        element: {
          value: this,
          writable: false,
          enumerable: true
        }
      });
      Object.defineProperty(this, '_classList', {
        value: classList,
        writable: false,
        enumerable: false
      });
      return classList;
    },
    enumerable: true
  });
})();
(function checkConsoleCompatibility() {
  if (!('console' in window)) {
    window.console = {
      log: function() {},
      error: function() {},
      warn: function() {}
    };
  } else if (!('bind' in console.log)) {
    console.log = (function(fn) {
      return function(msg) { return fn(msg); };
    })(console.log);
    console.error = (function(fn) {
      return function(msg) { return fn(msg); };
    })(console.error);
    console.warn = (function(fn) {
      return function(msg) { return fn(msg); };
    })(console.warn);
  }
})();
(function checkOnClickCompatibility() {
  function ignoreIfTargetDisabled(event) {
    if (isDisabled(event.target)) {
      event.stopPropagation();
    }
  }
  function isDisabled(node) {
    return node.disabled || (node.parentNode && isDisabled(node.parentNode));
  }
  if (navigator.userAgent.indexOf('Opera') !== -1) {
    document.addEventListener('click', ignoreIfTargetDisabled, true);
  }
})();
(function checkOnBlobSupport() {
  if (navigator.userAgent.indexOf('Trident') >= 0) {
    PDFJS.disableCreateObjectURL = true;
  }
})();
(function checkNavigatorLanguage() {
  if ('language' in navigator) {
    return;
  }
  PDFJS.locale = navigator.userLanguage || 'en-US';
})();
(function checkRangeRequests() {
  var isSafari = Object.prototype.toString.call(
                  window.HTMLElement).indexOf('Constructor') > 0;
  var regex = /Android\s[0-2][^\d]/;
  var isOldAndroid = regex.test(navigator.userAgent);
  var isChromeWithRangeBug = /Chrome\/(39|40)\./.test(navigator.userAgent);
  if (isSafari || isOldAndroid || isChromeWithRangeBug) {
    PDFJS.disableRange = true;
    PDFJS.disableStream = true;
  }
})();
(function checkHistoryManipulation() {
  if (!history.pushState || navigator.userAgent.indexOf('Android 2.') >= 0) {
    PDFJS.disableHistory = true;
  }
})();
(function checkSetPresenceInImageData() {
  if (window.CanvasPixelArray) {
    if (typeof window.CanvasPixelArray.prototype.set !== 'function') {
      window.CanvasPixelArray.prototype.set = function(arr) {
        for (var i = 0, ii = this.length; i < ii; i++) {
          this[i] = arr[i];
        }
      };
    }
  } else {
    var polyfill = false, versionMatch;
    if (navigator.userAgent.indexOf('Chrom') >= 0) {
      versionMatch = navigator.userAgent.match(/Chrom(e|ium)\/([0-9]+)\./);
      polyfill = versionMatch && parseInt(versionMatch[2]) < 21;
    } else if (navigator.userAgent.indexOf('Android') >= 0) {
      polyfill = /Android\s[0-4][^\d]/g.test(navigator.userAgent);
    } else if (navigator.userAgent.indexOf('Safari') >= 0) {
      versionMatch = navigator.userAgent.
        match(/Version\/([0-9]+)\.([0-9]+)\.([0-9]+) Safari\//);
      polyfill = versionMatch && parseInt(versionMatch[1]) < 6;
    }
    if (polyfill) {
      var contextPrototype = window.CanvasRenderingContext2D.prototype;
      var createImageData = contextPrototype.createImageData;
      contextPrototype.createImageData = function(w, h) {
        var imageData = createImageData.call(this, w, h);
        imageData.data.set = function(arr) {
          for (var i = 0, ii = this.length; i < ii; i++) {
            this[i] = arr[i];
          }
        };
        return imageData;
      };
      contextPrototype = null;
    }
  }
})();
(function checkRequestAnimationFrame() {
  function fakeRequestAnimationFrame(callback) {
    window.setTimeout(callback, 20);
  }
  var isIOS = /(iPad|iPhone|iPod)/g.test(navigator.userAgent);
  if (isIOS) {
    window.requestAnimationFrame = fakeRequestAnimationFrame;
    return;
  }
  if ('requestAnimationFrame' in window) {
    return;
  }
  window.requestAnimationFrame =
    window.mozRequestAnimationFrame ||
    window.webkitRequestAnimationFrame ||
    fakeRequestAnimationFrame;
})();
(function checkCanvasSizeLimitation() {
  var isIOS = /(iPad|iPhone|iPod)/g.test(navigator.userAgent);
  var isAndroid = /Android/g.test(navigator.userAgent);
  if (isIOS || isAndroid) {
    PDFJS.maxCanvasPixels = 5242880;
  }
})();
(function checkFullscreenSupport() {
  var isEmbeddedIE = (navigator.userAgent.indexOf('Trident') >= 0 &&
                      window.parent !== window);
  if (isEmbeddedIE) {
    PDFJS.disableFullscreen = true;
  }
})();
(function checkCurrentScript() {
  if ('currentScript' in document) {
    return;
  }
  Object.defineProperty(document, 'currentScript', {
    get: function () {
      var scripts = document.getElementsByTagName('script');
      return scripts[scripts.length - 1];
    },
    enumerable: true,
    configurable: true
  });
})();
