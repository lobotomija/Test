import React, { StrictMode } from 'react'
import { Route, Router, Routes } from 'react-router'
import CardTable from './screens/CardTable'
import react from 'react'

const Root: React.FC = () => {
  return (
    <StrictMode>
      <Routes>
        <Route path='/' element={<CardTable />} index />
      </Routes>
    </StrictMode>
  )
}

export default Root
