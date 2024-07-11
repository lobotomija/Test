import React, { StrictMode } from 'react'
import { Route, Router, Routes } from 'react-router'
import NewCardTable from './screens/NewCardTable'
import react from 'react'

const Root: React.FC = () => {
  const styles = {
    li: {
      margin: '10px',
      color: '#121212',
      listStyle: 'none',
    },
  }

  return (
    <StrictMode>
      <Routes>
        <Route path='/' element={<NewCardTable />} index />
      </Routes>
    </StrictMode>
  )
}

export default Root
