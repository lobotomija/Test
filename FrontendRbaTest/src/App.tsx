import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import CardTable from './screens/CardTable';
//import './App.css';

export default function App({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <BrowserRouter>
      <div>
        <CardTable />
      </div>
    </BrowserRouter>
  );
}