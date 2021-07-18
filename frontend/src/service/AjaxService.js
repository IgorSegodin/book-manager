import CommonConfig from "@/service/CommonConfig";

function _internalFetch(url, params) {
    params = params || {};

    return fetch(CommonConfig.buildApiPath(url), params)
        .then((response) => {
            if (!response.ok) {
                return Promise.reject(response);
            } else {
                const contentType = response.headers.get('content-type');
                return contentType && contentType.indexOf('application/json') !== -1 ? response.json() : Promise.resolve(response);
            }
        });
}

export default {
    /**
     * @param {String} url - relative URL, Context path will be prepended automatically.
     * @param {Object} params - standard 'fetch' params
     * @return {Promise<any>} unwraps JSON if response contains header 'content-type: application/json'
     */
    fetch(url, params) {
        return _internalFetch(url, params);
    },

    /**
     * Download file
     * @param url - relative URL, Context path will be prepended automatically.
     * @param params - standard 'fetch' params
     * @return {Promise<void>}
     */
    download(url, params) {
        return _internalFetch(url, params)
            .then((response) => {
                const filename = response.headers.get('Content-Disposition').match(/filename="(.+)"/)[1];
                return response.blob()
                    .then(blob => {
                        return new Promise((resolve, reject) => {
                            const url = URL.createObjectURL(blob);

                            const a = document.createElement('a');
                            a.href = url;
                            a.setAttribute('download', filename);
                            a.click();
                            a.remove();
                            URL.revokeObjectURL(url);
                            resolve();
                        });
                    });
            });
    }
}