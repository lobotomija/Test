import * as React from 'react';
import { useState, useEffect, useRef } from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import TextField from '@mui/material/TextField';
import { styled } from "@mui/material/styles";
import { sendRequest } from '../utils/HttpRequest'
import { Button, Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';

export interface NewCard
{
  firstName: string,
  lastName: string,
  oib: string,
  status: string
}

export default function NewCardTable() {
  let [newCards,setNewCards]=useState([])
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [oib, setOib] = useState('');
  const [status, setStatus] = useState('');
  const [newCard, setNewCard] = useState({
      firstName: '',
      lastName: '',
      oib: '',
      status: '',
    })
  const [open, setOpen] = useState(false);

  async function handleNewCards() {
    const {result, error} = await sendRequest('GET', null, process.env.REACT_APP_API_URL + '/card_request/find_all')
    if(result != undefined) {
        setNewCards(result)
    } else if(error != undefined) {
        setNewCards([]);
        toast.error(error);
    }
  }

  const handleClickDelete = async (oib) =>{
    var shouldDelete = confirm(
      "Do you really want to delete this newCard data?"
    );

    if (shouldDelete) {
      const {result, error} = await sendRequest('DEL', null, process.env.REACT_APP_API_URL + '/card_request/' + oib)
      handleNewCards();
      if(error != undefined) {
          toast.error(error);
      }
    }
  }

  async function handleData() {
    const {result, error} = await sendRequest('GET', null, process.env.REACT_APP_API_URL + '/card_request/' + oib)
    if(result != undefined) {
        let array: any = [];
        array.push(result)
        setNewCards(array)
    } else if(error != undefined) {
        setNewCards([]);
        toast.error(error);
    }
  }

  const addDataSource = async () =>{
    const newCardData: NewCard = {
      firstName: firstName,
      lastName: lastName,
      oib: oib,
      status: status
    }
    const {result, error} = await sendRequest('POST', newCardData, process.env.REACT_APP_API_URL + '/card_request')
      if(result != undefined) {
          handleNewCards()
          setOib('')
      } else if(error != undefined) {
          toast.error(error);
      }
      setOpen(false);
  }

  const handleClose = () => setOpen(false);
  const handleClickOpen = () =>
  {
    setFirstName('')
    setLastName('')
    setOib('')
    setStatus('')
    setOpen(true);
  }

  const useOnceEffect = (effect) => {
      const initialRef = useRef(true);

      useEffect(() => {
        if (!initialRef.current) {
          return;
        }
        initialRef.current = false;
        effect();
      }, [effect]);
  }

    useOnceEffect(() => {
      handleNewCards();
    })


  const handleParamFirstName = name => e => {
    setFirstName(e.target.value)
  }

  const handleParamLastName = name => e => {
    setLastName(e.target.value)
  }

  const handleParamOIB = name => e => {
    setOib(e.target.value)
  }

  const handleParamStatus = name => e => {
    setStatus(e.target.value)
  }

  const StyledTableRow = styled(TableRow)(({ theme }) => ({
        '&:nth-of-type(odd)': {
          backgroundColor: "white",
        },
        '&:nth-of-type(even)': {
          backgroundColor: "lightgrey",
        },
  }));
  return (
    <div>
        <div>
            <ToastContainer />
        </div>
        <TextField
          margin="dense"
          id="oib"
          label="OIB"
          type="text"
          value={oib || ''}
          onChange={handleParamOIB('oib')}
          fullWidth
        />
        <Button variant="contained" color="primary" onClick={handleData}>
            Search
        </Button>
          <br/>
          <br/>
        <Button variant="contained" color="primary" onClick={() => handleClickOpen()}>
                Create New
              </Button>
              <Dialog
                open={open}
                onClose={handleClose}
                aria-labelledby="form-dialog-title"
              >
                <DialogTitle id="form-dialog-title">Create new card data</DialogTitle>
                  <DialogContent>
                      <TextField
                        margin="dense"
                        id="firstName_search"
                        label="First name"
                        type="text"
                        value={firstName || ''}
                        onChange={handleParamFirstName('firstName')}
                        fullWidth
                      />
                      <TextField
                        margin="dense"
                        id="lastName_search"
                        label="Last name"
                        type="text"
                        value={lastName || ''}
                        onChange={handleParamLastName('lastName')}
                        fullWidth
                      />
                      <TextField
                        margin="dense"
                        id="oib"
                        label="OIB"
                        type="text"
                        value={oib || ''}
                        onChange={handleParamOIB('oib')}
                        fullWidth
                      />
                      <TextField
                        margin="dense"
                        id="status"
                        label="Status"
                        type="text"
                        value={status || ''}
                        onChange={handleParamStatus('status')}
                        fullWidth
                      />
                  </DialogContent>
                  <DialogActions>
                    <Button onClick={handleClose} color="primary">
                      Cancel
                    </Button>
                    <Button onClick={addDataSource} color="primary">
                      Add
                    </Button>
                  </DialogActions>
              </Dialog>
    <Paper>
      <h1 style={{ textAlign: "center", color: "green" }}>
          NewCards table
      </h1>
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>First name</TableCell>
            <TableCell align="right">Last name</TableCell>
            <TableCell align="right">OIB</TableCell>
            <TableCell align="right">Status</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {newCards.map((newCard: NewCard) => (
            <StyledTableRow
              key={newCard.oib}
              sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
            >
              <TableCell scope="row">
                {newCard.firstName}
              </TableCell>
              <TableCell align="right">{newCard.lastName}</TableCell>
              <TableCell align="right">{newCard.oib}</TableCell>
              <TableCell align="right">{newCard.status}</TableCell>
                <TableCell padding="checkbox">
                   <Button onClick={() => handleClickDelete(newCard.oib)}>
                    <CloseIcon />
                   </Button>
                </TableCell>
            </StyledTableRow>

          ))}
        </TableBody>
      </Table>
    </TableContainer>
 </Paper>
 </div>
  );
}