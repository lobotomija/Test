import axios from 'axios';

export const sendRequest =  ( async (type: string, data: object | null, url: string) => {

    let error;
    let result;

    if(type == 'GET')
    {
        try {
            let res = await axios.get(url)
            let st = await res.data;
            result = JSON.parse(st.message)
        } catch(err: any) {
            error = err.response.data.description !== undefined ? err.response.data.description : err.response.data.error;
        }
    }
    else if(type == 'POST')
    {
        try {
            let res = await axios.post(url, data)
            let st = await res.data;
            result = JSON.parse(st.message)
        } catch(err: any) {
            error = err.response.data.description !== undefined ? err.response.data.description : err.response.data.error;
        }
    }
    else if(type == 'DEL')
    {
        try {
            let res = await axios.delete(url)
            await res.data;
            result = undefined
        } catch(err: any) {
            error = err.response.data.description !== undefined ? err.response.data.description : err.response.data.error;
        }
    }
    else if(type == 'PUT')
    {
        try {
            let res = await axios.put(url, data)
            let st = await res.data;
            result = JSON.parse(st.message)
        } catch(err: any) {
            error = err.response.data.description !== undefined ? err.response.data.description : err.response.data.error;
        }
    }

    return {result, error}
})