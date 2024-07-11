import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import NewCardTable from './screens/NewCardTable';
//import './App.css';

export default function App({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <BrowserRouter>
      <div>
        <NewCardTable />
      </div>
    </BrowserRouter>
  );
}