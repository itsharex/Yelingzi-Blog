import request, { buildQuery } from '@/utils/request'

export const getAlbumListService = () => request.get('/api/album/list')

export const getSimpleAlbumOfPhotoCountService = () => request.get('/api/album/photo/count')

export const getAlbumDataService = (album: number) => request.get(buildQuery('/api/album/data', {albumID: album}))

export const getBgListService = () => request.get('/api/background/list')
