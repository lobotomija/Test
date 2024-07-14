const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin'); 
const dotenv = require('dotenv');
const webpack = require('webpack');

// Load environment variables from .env file
const env = dotenv.config().parsed;

// Convert the environment variables to a format that DefinePlugin can use
const envKeys = Object.keys(env).reduce((prev, next) => {
  prev[`process.env.${next}`] = JSON.stringify(env[next]);
  return prev;
}, {});

module.exports = {
  mode: 'development',
  entry: './src/index.tsx',
  output: {
    filename: 'bundle.[contenthash].js',
    path: path.resolve(__dirname, 'dist'),
    clean: true,
    publicPath: '/',
  },
  resolve: {
    extensions: ['.ts', '.tsx', '.js'],
  },
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: [
              '@babel/preset-env',
              '@babel/preset-react',
              '@babel/preset-typescript'
            ]
          }
        },
        exclude: /node_modules/,
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader'],
      },
    ],
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './public/index.html',
    }),
    new CleanWebpackPlugin(),
    new webpack.DefinePlugin(envKeys),
  ],
  devtool: 'inline-source-map',
  devServer: {
    static: {
      directory: path.join(__dirname, 'dist'),
    },
    hot: true,
    client: {
      overlay: false,
      logging: 'log',
    },
    compress: true,
    port: 8214,
    proxy: [
      {      
        path: '/v1',
        target: 'http://127.0.0.1:8081/v1',
        changeOrigin: false,
        secure: false,
        onProxyReq: proxyReq => {
          if (proxyReq.getHeader('origin')) {
            proxyReq.setHeader('origin', 'http://127.0.0.1:8081/');
          }
        }
      }
    ],
    historyApiFallback: true, 
  },
  performance: {
    hints: false
  },
};