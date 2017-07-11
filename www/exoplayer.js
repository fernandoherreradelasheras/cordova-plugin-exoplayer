/*global cordova, module*/
/*
 The MIT License (MIT)

 Copyright (c) 2017 Nedim Cholich

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

var utils = require('cordova/utils');

module.exports = {
    create: function (parameters, successCallback, errorCallback) {
        var id = utils.createUUID();
        cordova.exec(successCallback, errorCallback, "ExoPlayer", "create", [id, parameters]);
	return id;
    },
    setStream: function (id, url, controller, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ExoPlayer", "setStream", [id, url, controller]);
    },
    playPause: function (id, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ExoPlayer", "playPause", [id]);
    },
    stop: function (id, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ExoPlayer", "stop", [id]);
    },
    seekTo: function (id, milliseconds, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ExoPlayer", "seekTo", [id, milliseconds]);
    },
    getState: function (id, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ExoPlayer", "getState", [id]);
    },
    showController: function (id, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ExoPlayer", "showController", [id]);
    },
    hideController: function (id, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ExoPlayer", "hideController", [id]);
    },
    close: function (id, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ExoPlayer", "close", [id]);
    },
    closeAll: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ExoPlayer", "closeAll", []);
    }
};
