import argparse
import asyncio
import traceback
import logging
import pathlib
import pyrogram
import pyrogram.file_id

async def run_bot(args, func):
    async with pyrogram.Client(args.bot_username, api_id=args.api_id,
                 api_hash=args.api_hash, bot_token=args.bot_token) as bot_client:
        await func(bot_client)

async def get_file(args):
    args.outputpath.resolve()
    if args.outputpath.is_dir():
        raise RuntimeError('Should be file')

    async def download_media_func(client: pyrogram.Client):
        file_id_obj = pyrogram.file_id.FileId.decode(args.file_id)
        await client.handle_download((file_id_obj, args.outputpath.parent, args.outputpath.name, False, 0, None, tuple()))

    await run_bot(args, download_media_func)

async def put_file(args):
    args.input.resolve()
    if not args.input.exists():
        raise RuntimeError('Input not exists')

    async def upload_file_func(client: pyrogram.Client):
        if args.type == 'audio':
            await client.send_audio(args.chat_id, args.input)
        elif args.type == 'video':
            await client.send_video(args.chat_id, args.input)
        elif args.type == 'gif':
            await client.send_animation(args.chat_id, args.input)

    await run_bot(args, upload_file_func)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(prog = 'pytgfile', description = 'Get or put Telegram files by file id')
    parser.add_argument('--api_id', type=str, required=True)
    parser.add_argument('--api_hash', type=str, required=True)
    parser.add_argument('--bot_username', type=str, required=True)
    parser.add_argument('--bot_token', type=str, required=True)
    subparsers = parser.add_subparsers(dest='command', required=True)

    parser_get = subparsers.add_parser('get', help='get help')
    parser_get.set_defaults(func=get_file)
    parser_get.add_argument('--file_id', type=str, help='bar help', required=True)
    parser_get.add_argument('-o', '--outputpath', type=pathlib.Path, help='bar help', required=True)
 
    parser_put = subparsers.add_parser('put', help='put help')
    parser_put.set_defaults(func=put_file)
    parser_put.add_argument('-c', '--chat_id', type=str, help='chat_id help', required=True)
    parser_put.add_argument('-i', '--input', type=pathlib.Path, help='input help', required=True)
    parser_put.add_argument('-t', '--type', type=str, help='type help', choices=['audio', 'video', 'gif'], required=True)
    args = parser.parse_args()

    try:
        asyncio.run(args.func(args))
    except Exception as e:
        logging.error(traceback.format_exc())
    except:
        exit(1)

    exit(0)