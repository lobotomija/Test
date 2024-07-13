import axios from 'axios';

export const sendRequest =  ( async (type, data, url) => {

    let error;
    let result;

    if(type == 'GET')
    {
        try {
            let res = await axios.get(url)
            let st = await res.data;
            result = JSON.parse(st.message)
        } catch(err) {
            error = err.response.data.description !== undefined ? err.response.data.description : "Bad request";
        }
    }
    else if(type == 'POST')
    {
        try {
            let res = await axios.post(url, data)
            let st = await res.data;
            result = JSON.parse(st.message)
        } catch(err) {
            error = err.response.data.description !== undefined ? err.response.data.description : "Bad request";
        }
    }
    else if(type == 'DEL')
    {
        try {
            let res = await axios.delete(url)
            let st = await res.data;
            result = undefined
        } catch(err) {
            error = err.response.data.description !== undefined ? err.response.data.description : "Bad request";
        }
    }
    else if(type == 'PUT')
    {
        try {
            let res = await axios.put(url, data)
            let st = await res.data;
            result = JSON.parse(st.message)
        } catch(err) {
            error = err.response.data.description !== undefined ? err.response.data.description : "Bad request";
        }
    }

    return {result, error}
})