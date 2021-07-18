function _buildPath(prefix, url) {
    return ('/' + prefix + '/' + url)
        // Replace all types of space (tabs, new lines etc.) with empty string
        .replace(/[\s]+/g, '')
        // Replace multiple slashes with single one
        .replace(/[/\\]+/g, '/');
}

export default {

    buildContextPath: function (inputUrl) {
        return _buildPath(process.env.VUE_APP_CONTEXT_PATH, inputUrl);
    },

    buildApiPath: function (inputUrl) {
        return _buildPath(process.env.VUE_APP_API_PATH, inputUrl);
    }

}


