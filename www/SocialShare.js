var exec = require('cordova/exec');

exports.wechatShareText = function (arg0, success, error) {
    exec(success, error, 'SocialShare', 'wechatShareText', [arg0]);
};
exports.wechatShareImage = function (arg0, success, error) {
    exec(success, error, 'SocialShare', 'wechatShareImage', [arg0]);
};
exports.wechatShareWebPage = function (arg0, success, error) {
    exec(success, error, 'SocialShare', 'wechatShareWebPage', [arg0]);
};
exports.QQShareImageText = function (arg0, success, error) {
    exec(success, error, 'SocialShare', 'QQShareImageText', [arg0]);
};
exports.QQZoneShareImageText = function (arg0, success, error) {
    exec(success, error, 'SocialShare', 'QQZoneShareImageText', [arg0]);
};

var SocialShare={};

SocialShare.wechatShareText = function (arg0, success, error) {
    exec(success, error, 'SocialShare', 'wechatShareText', [arg0]);
};
SocialShare.wechatShareImage = function (arg0, success, error) {
    exec(success, error, 'SocialShare', 'wechatShareImage', [arg0]);
};
SocialShare.wechatShareWebPage = function (arg0, success, error) {
    exec(success, error, 'SocialShare', 'wechatShareWebPage', [arg0]);
};
SocialShare.QQShareImageText = function (arg0, success, error) {
    exec(success, error, 'SocialShare', 'QQShareImageText', [arg0]);
};
SocialShare.QQZoneShareImageText = function (arg0, success, error) {
    exec(success, error, 'SocialShare', 'QQZoneShareImageText', [arg0]);
};

module.exports = SocialShare;